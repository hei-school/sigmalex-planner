package school.hei.planner.constraint;

import org.junit.jupiter.api.Test;
import school.hei.planner.Occupation;
import school.hei.planner.Timetable;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Course;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Group;
import school.hei.planner.costly.Room;
import school.hei.planner.costly.Slot;
import school.hei.planner.costly.Teacher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PartitionedTimetableConstraintTest {


  private Timetable one_month_timetable() {
    var k1 = new Group("k1");
    var k2 = new Group("k2");
    var k3 = new Group("k3");
    var k4 = new Group("k4");

    var toky_availabilities = new ArrayList<Date>();
    for (int date = 1; date <= 31; date++) {
      toky_availabilities.add(new Date(2023, OCTOBER, date));
    }
    var toky = new Teacher("toky", toky_availabilities);
    var prog1 = new Course("prog1", Duration.ofHours(16));
    var ac_k1_prog1_toky = new AwardedCourse(prog1, k1, toky);
    var ac_k2_prog1_toky = new AwardedCourse(prog1, k2, toky);
    var ac_k3_prog1_toky = new AwardedCourse(prog1, k3, toky);
    var ac_k4_prog1_toky = new AwardedCourse(prog1, k4, toky);
    var web1 = new Course("web1", Duration.ofHours(10));
    var ac_k1_web1_toky = new AwardedCourse(web1, k1, toky);
    var ac_k2_web1_toky = new AwardedCourse(web1, k2, toky);
    var ac_k3_web1_toky = new AwardedCourse(web1, k3, toky);
    var ac_k4_web1_toky = new AwardedCourse(web1, k4, toky);
    var awarded_courses = new AwardedCourse[]{
        ac_k1_prog1_toky,
        ac_k2_prog1_toky,
        ac_k3_prog1_toky,
        ac_k4_prog1_toky,

        ac_k1_web1_toky,
        ac_k2_web1_toky,
        ac_k3_web1_toky,
        ac_k4_web1_toky
    };

    var ra = new Room("a");
    var rb = new Room("b");
    var rooms = new Room[]{ra, rb};

    var dates_all = new Date[31];
    for (int date = 1; date <= 31; date++) {
      dates_all[date - 1] = (new Date(2023, OCTOBER, date));
    }

    var dates_off = new Date[]{};

    Set<Occupation> occupations = Set.of();

    return new Timetable("id", awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);
  }

  @Test
  void can_partition() {
    var partitioned = new PartitionedTimetableConstraint(one_month_timetable(), 12);

    var partitions = partitioned.getPartitions();
    var partitionKeys = partitions.keySet().toArray(Timetable.Id[]::new);
    Arrays.sort(partitionKeys, Comparator.comparing(Timetable.Id::value, String::compareTo));
    assertEquals(3, partitionKeys.length);

    var timetable0 = partitions.get(partitionKeys[0]).timetable;
    assertEquals(12, timetable0.getDatesAll().length);
    assertEquals(8, timetable0.getAwardedCourses()[0].course().duration().toHours());
    assertEquals(4, timetable0.getAwardedCourses()[4].course().duration().toHours());

    var timetable1 = partitions.get(partitionKeys[1]).timetable;
    assertEquals(12, timetable1.getDatesAll().length);
    assertEquals(6, timetable1.getAwardedCourses()[0].course().duration().toHours());
    assertEquals(4, timetable1.getAwardedCourses()[4].course().duration().toHours());

    var timetable2 = partitions.get(partitionKeys[2]).timetable;
    assertEquals(7, timetable2.getDatesAll().length);
    assertEquals(2, timetable2.getAwardedCourses()[0].course().duration().toHours());
    assertEquals(2, timetable2.getAwardedCourses()[4].course().duration().toHours());
  }

  @Test
  void can_solve() {
    var partitioned = new PartitionedTimetableConstraint(one_month_timetable(), 12);

    var solutions = partitioned.solve();
    var partitionKeys = solutions.keySet().toArray(Timetable.Id[]::new);
    Arrays.sort(partitionKeys, Comparator.comparing(Timetable.Id::value, String::compareTo));
    assertEquals(3, partitionKeys.length);

    var solution0 = solutions.get(partitionKeys[0]);
    assertEquals(24, solution0.size());

    var solution1 = solutions.get(partitionKeys[1]);
    assertEquals(20, solution1.size());

    var solution2 = solutions.get(partitionKeys[2]);
    assertEquals(8, solution2.size());
  }
}