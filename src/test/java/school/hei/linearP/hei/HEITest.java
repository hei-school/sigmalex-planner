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
import java.util.Set;

import static java.time.Month.JULY;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearP.hei.Occupation.dateNameFromOccupation;
import static school.hei.linearP.hei.Occupation.roomNameFromOccupation;
import static school.hei.linearP.hei.Occupation.slotNameFromOccupation;

public class HEITest {

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
        new Date(2023, JULY, 28));
    var t2 = new Teacher(
        "t2",
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25),
        new Date(2023, JULY, 26),
        new Date(2023, JULY, 27));
    var t3 = new Teacher(
        "t3",
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 26),
        new Date(2023, JULY, 27));
    var th1 = new Course("th1", Duration.ofHours(6));
    var prog2 = new Course("prog2", Duration.ofHours(8));
    var sem1 = new Course("sem1", Duration.ofHours(2));
    var sys2p3 = new Course("sys2p3", Duration.ofHours(6));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g2_th1_t1 = new AwardedCourse(th1, g2, t1);
    var ac_g1_prog2_t2 = new AwardedCourse(prog2, g1, t2);
    var ac_g2_prog2_t2 = new AwardedCourse(prog2, g2, t2);
    var ac_g1_sem1_t2 = new AwardedCourse(sem1, g1, t2);
    var ac_g1_sys2p3_t3 = new AwardedCourse(sys2p3, g1, t3);
    var awarded_courses = new AwardedCourse[]{
        ac_g1_th1_t1, ac_g2_th1_t1, ac_g1_prog2_t2, ac_g2_prog2_t2, ac_g1_sem1_t2, ac_g1_sys2p3_t3};
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
    Set<Occupation> occupations = Set.of();
    var heiTimetable = new HEITimetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);

    var solution_occupations = new HEITimetableConstraint(heiTimetable).solve();

    assertEquals(
        """
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:a][s:f08t10]
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul20][r:b][s:f08t10]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul20][r:a][s:f10t12]
            occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul20][r:b][s:f13t15]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul23][r:a][s:f08t10]
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul23][r:b][s:f08t10]
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul23][r:a][s:f10t12]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul23][r:b][s:f10t12]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul24][r:a][s:f08t10]
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul24][r:b][s:f08t10]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul24][r:a][s:f10t12]
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul24][r:b][s:f10t12]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul25][r:a][s:f08t10]
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul25][r:b][s:f08t10]
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul25][r:a][s:f10t12]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul25][r:b][s:f10t12]
            occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul26][r:a][s:f08t10]
            occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul27][r:a][s:f08t10]""",
        toStringTimetable(solution_occupations));
  }

  private String toStringTimetable(Set<Occupation> occupations) {
    return occupations.stream()
        .sorted(this::compareOccupationEntry)
        .map(Occupation::toString)
        .collect(joining("\n"));
  }

  private int compareOccupationEntry(Occupation o1, Occupation o2) {
    var name1 = o1.toString();
    var name2 = o2.toString();

    var compareDates = dateNameFromOccupation(name1).compareTo(dateNameFromOccupation(name2));
    if (compareDates != 0) {
      return compareDates;
    }

    var compareSlots = slotNameFromOccupation(name1).compareTo(slotNameFromOccupation(name2));
    if (compareSlots != 0) {
      return compareSlots;
    }

    return roomNameFromOccupation(name1).compareTo(roomNameFromOccupation(name2));
  }
}
