package school.hei.linearP.hei.test;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.Occupation;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.hei.costly.Teacher;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.linearP.hei.Occupation.toOrderedLines;
import static school.hei.linearP.hei.constraint.HEITimetableConstraint.occupationFrom;

public class FeasibleHEITest {


  private HEITimetable timetable1() {
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
    var th1 = new Course("th1", Duration.ofHours(4));
    var prog2 = new Course("prog2", Duration.ofHours(6));
    var sem1 = new Course("sem1", Duration.ofHours(8));
    var sys2p3 = new Course("sys2p3", Duration.ofHours(4));
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
    return new HEITimetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);
  }

  @RepeatedTest(value = 1)
  public void sigmalex_the_wise_can_solve_feasible_timetable1() {
    var timetable_constraints = new HEITimetableConstraint(timetable1());

    var solution_occupations = timetable_constraints.solve();

    assertEquals(16, solution_occupations.size());
    assertEquals(
        """
            occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul20][r:a][s:f08t10]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:b][s:f08t10]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul20][r:b][s:f10t12]
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul20][r:b][s:f13t15]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul23][r:a][s:f08t10]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul23][r:b][s:f08t10]
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul23][r:a][s:f10t12]
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul23][r:b][s:f10t12]
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul23][r:a][s:f13t15]
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul25][r:a][s:f08t10]
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul25][r:b][s:f08t10]
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul25][r:a][s:f10t12]
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul25][r:b][s:f10t12]
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul25][r:b][s:f13t15]
            occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul26][r:b][s:f08t10]
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul27][r:a][s:f08t10]""",
        toOrderedLines(solution_occupations));
  }

  @RepeatedTest(value = 1)
  public void solution_to_timetable1_is_idempotent() {
    var timetable = timetable1();
    Set<Occupation> provided_occupations = new HashSet<>();
    addOccupation(provided_occupations, "occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul20][r:a][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:b][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g1][t:t2]][d:jul20][r:b][s:f10t12]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:sem1][g:g1][t:t2]][d:jul20][r:b][s:f13t15]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g1][t:t2]][d:jul23][r:a][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g2][t:t2]][d:jul23][r:b][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:th1][g:g1][t:t1]][d:jul23][r:a][s:f10t12]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:th1][g:g2][t:t1]][d:jul23][r:b][s:f10t12]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:sem1][g:g1][t:t2]][d:jul23][r:a][s:f13t15]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g2][t:t2]][d:jul25][r:a][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:sem1][g:g1][t:t2]][d:jul25][r:b][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:th1][g:g2][t:t1]][d:jul25][r:a][s:f10t12]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:th1][g:g1][t:t1]][d:jul25][r:b][s:f10t12]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:prog2][g:g1][t:t2]][d:jul25][r:b][s:f13t15]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:sys2p3][g:g1][t:t3]][d:jul26][r:b][s:f08t10]", timetable);
    addOccupation(provided_occupations, "occupation[ac:[c:sem1][g:g1][t:t2]][d:jul27][r:a][s:f08t10]I", timetable);
    HEITimetableConstraint solved_timetable_constraints = new HEITimetableConstraint(
        timetable.withOccupations(provided_occupations));

    var solution_occupations = solved_timetable_constraints.solve();

    assertEquals(provided_occupations, solution_occupations);
  }

  private void addOccupation(Set<Occupation> occupations, String occupationName, HEITimetable timetable) {
    occupations.add(occupationFrom(occupationName, timetable));
  }

  @Test
  public void sigmalex_the_wise_understands_that_timetable1_has_no_violation() {
    var timetable_constraints = new HEITimetableConstraint(timetable1());
    assertTrue(timetable_constraints.detectViolations().isEmpty());
  }
}
