package school.hei.linearP.hei;

import org.junit.jupiter.api.RepeatedTest;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.hei.costly.Teacher;

import java.time.Duration;
import java.util.Map;
import java.util.regex.Pattern;

import static java.time.Month.JULY;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HEITest {

  private static final Pattern OCCUPATION_VAR_PATTERN = Pattern.compile(
      "occupation\\[ac:\\[c:(.*)]\\[g:(.*)]\\[t:(.*)]]\\[d:(.*)]\\[r:(.*)]\\[s:(.*)]");

  @RepeatedTest(value = 1)
  public void ask_sigmalex_the_wise_to_plan_hei() {
    var g1 = new Group("g1");
    var g2 = new Group("g2");
    var t1 = new Teacher(
        "t1",
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25),
        new Date(2023, JULY, 26),
        new Date(2023, JULY, 27),
        new Date(2023, JULY, 28));
    var t2 = new Teacher(
        "t2",
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25),
        new Date(2023, JULY, 26),
        new Date(2023, JULY, 27),
        new Date(2023, JULY, 28));
    var th1 = new Course("th1", Duration.ofHours(6));
    var prog2 = new Course("prog2", Duration.ofHours(8));
    var sem1 = new Course("sem1", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g2_th1_t1 = new AwardedCourse(th1, g2, t1);
    var ac_g1_prog2_t2 = new AwardedCourse(prog2, g1, t2);
    var ac_g2_prog2_t2 = new AwardedCourse(prog2, g2, t2);
    var ac_g1_sem1_t2 = new AwardedCourse(sem1, g1, t2);
    var awarded_courses = new AwardedCourse[]{
        ac_g1_th1_t1, ac_g2_th1_t1, ac_g1_prog2_t2, ac_g2_prog2_t2, ac_g1_sem1_t2};
    var ra = new Room("a");
    var rb = new Room("b");
    var rooms = new Room[]{ra, rb};
    var dates_all = new Date[]{
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25),
        new Date(2023, JULY, 26),
        new Date(2023, JULY, 27),
        new Date(2023, JULY, 28)};
    var dates_off = new Date[]{ // to be enriched later on with slots_off
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22)};
    var occupations = new Occupation[]{};
    var heiTimetable = new HEITimetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);

    var actual_solution = new HEITimetableConstraint(heiTimetable).solve();

    assertEquals(1.219222E7, actual_solution.optimalObjective());
    assertEquals(
        """
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:a][s:f08t10]=1.0
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul20][r:b][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul20][r:b][s:f10t12]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul23][r:a][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul23][r:b][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul23][r:a][s:f10t12]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul23][r:b][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul24][r:a][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul24][r:b][s:f08t10]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul24][r:a][s:f10t12]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul24][r:b][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul25][r:a][s:f08t10]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul25][r:b][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul25][r:a][s:f10t12]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul25][r:b][s:f10t12]=1.0""",
        toStringTimetable(actual_solution.optimalBoundedVariablesForUnboundedName("occupation")));
  }

  private String toStringTimetable(Map<String, Double> occupations) {
    return occupations.entrySet().stream()
        .sorted(this::compareOccupationEntry)
        .map(Map.Entry::toString)
        .collect(joining("\n"));
  }

  private int compareOccupationEntry(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
    var name1 = entry1.getKey();
    var matcher1 = OCCUPATION_VAR_PATTERN.matcher(name1);
    if (!matcher1.find()) {
      throw new RuntimeException("Variable name does not follow expected pattern: " + name1);
    }

    var name2 = entry2.getKey();
    var matcher2 = OCCUPATION_VAR_PATTERN.matcher(name2);
    if (!matcher2.find()) {
      throw new RuntimeException("Variable name does not follow expected pattern: " + name2);
    }

    var compareDates = matcher1.group(4).compareTo(matcher2.group(4));
    if (compareDates != 0) {
      return compareDates;
    }
    var compareSlots = matcher1.group(6).compareTo(matcher2.group(6));
    if (compareSlots != 0) {
      return compareSlots;
    }
    return matcher1.group(5).compareTo(matcher2.group(5)); // room
  }
}
