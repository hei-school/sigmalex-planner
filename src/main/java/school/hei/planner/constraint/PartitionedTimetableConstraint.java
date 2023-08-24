package school.hei.planner.constraint;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.SneakyThrows;
import school.hei.planner.Occupation;
import school.hei.planner.Timetable;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Slot;
import school.hei.sigmalex.concurrency.Workers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class PartitionedTimetableConstraint {

  private static final int DEFAULT_DAYS_NB_IN_PARTITION = 7;
  static AtomicInteger id = new AtomicInteger(0);
  @Getter
  private final Map<Timetable.Id, TimetableConstraint> partitions;

  public PartitionedTimetableConstraint(Timetable timetable, int daysNbInPartition) {
    this.partitions = groupByTimetableId(lowerAcDurations(
        partition(new TimetableConstraint(timetable), daysNbInPartition),
        timetable));
  }

  public PartitionedTimetableConstraint(Timetable timetable) {
    this(timetable, DEFAULT_DAYS_NB_IN_PARTITION);
  }

  private static List<TimetableConstraint> partition(
      TimetableConstraint timetableConstraint, int daysNbInPartition) {
    var sortedDays = Arrays.stream(timetableConstraint.timetable.getDatesAll())
        .sorted(comparing(Date::getLocalDate))
        .toList();
    var partitionedDays = Lists.partition(sortedDays, daysNbInPartition);
    return partitionedDays.stream().map(
            partition -> new TimetableConstraint(timetableConstraint.timetable
                .withId(timetableConstraint.timetable.getId() + "_partition-" + id.getAndIncrement())
                .withDatesAll(partition.toArray(Date[]::new))
                .withAwardedCourses(Arrays.stream(timetableConstraint.timetable.getAwardedCourses())
                    .map(ac -> ac.withCourse(ac.course()
                        .withDuration(proportionalDuration(
                            ac.course().duration(), sortedDays.size(), partition.size()))))
                    .toArray(AwardedCourse[]::new))))
        .collect(toList());
  }

  private static Duration proportionalDuration(Duration duration, double full, double partial) {
    return Duration.ofHours(
        Slot.DURATION.toHours() * (long) Math.ceil( //note(lower-afterwards)
            duration.toHours() * partial / full
                / Slot.DURATION.toHours()));
  }

  private Map<Timetable.Id, TimetableConstraint> groupByTimetableId(List<TimetableConstraint> tcList) {
    var res = new HashMap<Timetable.Id, TimetableConstraint>();
    for (var tc : tcList) {
      res.put(tc.timetable.getId(), tc);
    }
    return res;
  }

  private List<TimetableConstraint> lowerAcDurations(List<TimetableConstraint> partitions, Timetable timetable) {
    List<TimetableConstraint> res = new ArrayList<>(partitions);
    for (var ac : timetable.getAwardedCourses()) {
      res = lowerAcDurations(res, ac);
    }
    return res;
  }

  private List<TimetableConstraint> lowerAcDurations(List<TimetableConstraint> partitions, AwardedCourse ac) {
    var targetHours = ac.durationInHours();
    var partitionsHours = partitions.stream()
        .flatMap(timetableConstraint -> Arrays.stream(timetableConstraint.timetable.getAwardedCourses()))
        .filter(lambdaAc -> lambdaAc.equalsExceptDuration(ac))
        .map(AwardedCourse::durationInHours)
        .reduce(0L, Long::sum);
    var hoursToRemove = partitionsHours - targetHours;
    if (hoursToRemove < 0 || hoursToRemove % Slot.DURATION.toHours() != 0) {
      //note(lower-afterwards)
      throw new RuntimeException("hoursToRemove has unexpectedValue: " +
          "are you sure that partitions was built from PartitionedTimetableConstraint::Partition?");
    }

    var resArray = partitions.toArray(TimetableConstraint[]::new);
    Arrays.sort(resArray, comparing(timetableConstraint -> Arrays.stream(timetableConstraint.getTimetable().getDatesAll())
        .sorted(comparing(Date::getLocalDate))
        .toList()
        .get(0)
        .getLocalDate(), LocalDate::compareTo));

    int partitionsSize = partitions.size();
    int partitionsSizeDecr = 1;
    int partitionIdx;
    while (hoursToRemove != 0) {
      partitionIdx = partitionsSize - partitionsSizeDecr;
      resArray[partitionIdx] = new TimetableConstraint(resArray[partitionIdx].timetable
          .withAwardedCourses(
              Arrays.stream(resArray[partitionIdx].timetable.getAwardedCourses())
                  .map(lambdaAc -> lambdaAc.equalsExceptDuration(ac) ?
                      //TODO(re-enable-duration-equality):
                      //  the following bothers noDuplicateName if duration equality enabled
                      lambdaAc.withDurationInHours(lambdaAc.durationInHours() - Slot.DURATION.toHours()) : lambdaAc)
                  .toArray(AwardedCourse[]::new)));
      hoursToRemove = hoursToRemove - Slot.DURATION.toHours();
      partitionsSizeDecr = (partitionsSizeDecr + 1) % partitionsSize;
    }
    return Arrays.stream(resArray).toList();
  }

  @SneakyThrows
  public Map<Timetable.Id, Set<Occupation>> solve() {
    var res = new HashMap<Timetable.Id, Set<Occupation>>();
    Workers.submit(() -> partitions.keySet().parallelStream()
            .peek(key -> res.put(key, partitions.get(key).solve()))
            .collect(Collectors.toSet()))
        .get();
    return res;
  }
}
