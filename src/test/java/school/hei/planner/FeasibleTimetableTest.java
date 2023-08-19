package school.hei.planner;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Course;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Group;
import school.hei.planner.costly.Room;
import school.hei.planner.costly.Slot;
import school.hei.planner.costly.Teacher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.planner.Occupation.toOrderedLines;

public class FeasibleTimetableTest {


  private Timetable timetable1() {
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
    return new Timetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);
  }

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
    var web1 = new Course("web1", Duration.ofHours(16));
    var ac_k1_web1_toky = new AwardedCourse(web1, k1, toky);
    var ac_k2_web1_toky = new AwardedCourse(web1, k2, toky);
    var ac_k3_web1_toky = new AwardedCourse(web1, k3, toky);
    var ac_k4_web1_toky = new AwardedCourse(web1, k4, toky);

    var sandrine = new Teacher("sandrine",
        new Date(2023, OCTOBER, 3),
        new Date(2023, OCTOBER, 7),
        new Date(2023, OCTOBER, 11),
        new Date(2023, OCTOBER, 15),
        new Date(2023, OCTOBER, 19),
        new Date(2023, OCTOBER, 23),
        new Date(2023, OCTOBER, 27));
    var mgt1 = new Course("mgt1", Duration.ofHours(8));
    var ac_k1_mgt1_sandrine = new AwardedCourse(mgt1, k1, sandrine);
    var ac_k2_mgt1_sandrine = new AwardedCourse(mgt1, k2, sandrine);
    var ac_k3_mgt1_sandrine = new AwardedCourse(mgt1, k3, sandrine);
    var ac_k4_mgt1_sandrine = new AwardedCourse(mgt1, k4, sandrine);

    var seth = new Teacher("seth",
        new Date(2023, OCTOBER, 2),
        new Date(2023, OCTOBER, 6),
        new Date(2023, OCTOBER, 10),
        new Date(2023, OCTOBER, 14),
        new Date(2023, OCTOBER, 18),
        new Date(2023, OCTOBER, 22),
        new Date(2023, OCTOBER, 26));
    var lv1 = new Course("lv1", Duration.ofHours(8));
    var ac_k1_lv1_seth = new AwardedCourse(lv1, k1, seth);
    var ac_k2_lv1_seth = new AwardedCourse(lv1, k2, seth);
    var ac_k3_lv1_seth = new AwardedCourse(lv1, k3, seth);
    var ac_k4_lv1_seth = new AwardedCourse(lv1, k4, seth);

    var h1 = new Group("h1");
    var h2 = new Group("h2");
    var h3 = new Group("h3");
    var h4 = new Group("h4");
    var sitraka = new Teacher("sitraka",
        new Date(2023, OCTOBER, 4),
        new Date(2023, OCTOBER, 8),
        new Date(2023, OCTOBER, 12),
        new Date(2023, OCTOBER, 16),
        new Date(2023, OCTOBER, 20),
        new Date(2023, OCTOBER, 24),
        new Date(2023, OCTOBER, 28));
    var web3 = new Course("web3", Duration.ofHours(8));
    var ac_h1_web3_sitraka = new AwardedCourse(web3, h1, sitraka);
    var ac_h2_web3_sitraka = new AwardedCourse(web3, h2, sitraka);
    var ac_h3_web3_sitraka = new AwardedCourse(web3, h3, sitraka);
    var ac_h4_web3_sitraka = new AwardedCourse(web3, h4, sitraka);

    var awarded_courses = new AwardedCourse[]{
        ac_k1_prog1_toky,
        ac_k2_prog1_toky,
        ac_k3_prog1_toky,
        ac_k4_prog1_toky,

        ac_k1_web1_toky,
        ac_k2_web1_toky,
        ac_k3_web1_toky,
        ac_k4_web1_toky,

        ac_k1_mgt1_sandrine,
        ac_k2_mgt1_sandrine,
        ac_k3_mgt1_sandrine,
        ac_k4_mgt1_sandrine,

        ac_k1_lv1_seth,
        ac_k2_lv1_seth,
        ac_k3_lv1_seth,
        ac_k4_lv1_seth,

        ac_h1_web3_sitraka,
        ac_h2_web3_sitraka,
        ac_h3_web3_sitraka,
        ac_h4_web3_sitraka
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

    return new Timetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);
  }

  @RepeatedTest(value = 1)
  public void sigmalex_the_wise_can_solve_feasible_timetable1() {
    var timetable_constraints = new TimetableConstraint(timetable1());

    var solution_occupations = timetable_constraints.solve();

    assertEquals(16, solution_occupations.size());
    var solution_occupations_lines = toOrderedLines(solution_occupations);
    assertTrue(solution_occupations_lines.contains("jul20"));
    assertTrue(solution_occupations_lines.contains("jul27"));
  }

  @Test
  public void one_month_timetable_is_feasible() {
    var timetable_constraints = new TimetableConstraint(one_month_timetable());

    var solution_occupations =
        /* Takes around 6mn on 8cores CPU and 16Go RAM and -Xss68m
         * SigmaLex: 5mn. OrTools: 1mn.
         * Nb of MILP to solve: 1...
         * \ Generated by MPModelProtoExporter
         * \   Name             :
         * \   Format           : Free
         * \   Constraints      : 17921
         * \   Variables        : 10080
         * \     Binary         : 5120
         * \     Integer        : 0
         * \     Continuous     : 4960 */
        timetable_constraints.solve();

    assertFalse(solution_occupations.isEmpty());
  }

  @Test
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
    TimetableConstraint solved_timetable_constraints = new TimetableConstraint(
        timetable.withOccupations(provided_occupations));

    var solution_occupations = solved_timetable_constraints.solve();

    assertEquals(provided_occupations, solution_occupations);
  }

  private void addOccupation(Set<Occupation> occupations, String occupationName, Timetable timetable) {
    occupations.add(TimetableConstraint.occupationFrom(occupationName, timetable));
  }

  @Test
  public void sigmalex_the_wise_understands_that_timetable1_has_no_violation() {
    var timetable_constraints = new TimetableConstraint(timetable1());
    assertTrue(timetable_constraints.detectViolations().isEmpty());
  }
}
