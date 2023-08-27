package school.hei.planner.constraint.sub;

import org.junit.jupiter.api.Test;
import school.hei.planner.Occupation;
import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.constraint.Violation;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Course;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Group;
import school.hei.planner.costly.Location;
import school.hei.planner.costly.Room;
import school.hei.planner.costly.Slot;
import school.hei.planner.costly.Teacher;

import java.time.Duration;
import java.util.Set;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class a_teacher_cannot_move_location_half_a_day_test {

  @Test
  public void unfeasible_one_teacher() {
    var date1 = new Date(2023, JULY, 20);
    var g1 = new Group("g1");
    var t1 = new Teacher("t1", date1);
    var th1 = new Course("th1", Duration.ofHours(2));
    var prog2 = new Course("prog2", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g1_prog2_t1 = new AwardedCourse(prog2, g1, t1);
    var awarded_courses = new AwardedCourse[]{ac_g1_th1_t1, ac_g1_prog2_t1};
    var ra = new Room("a");
    var rb = new Room("b");
    var rc = new Room("c");
    var rd = new Room("d");
    var rooms = new Room[]{ra, rb, rc, rd};
    var locations = new Location[]{
        new Location("l1", Set.of(ra, rb)),
        new Location("l2", Set.of(rc, rd)),
    };

    var dates_all = new Date[]{date1};
    var dates_off = new Date[]{};
    Set<Occupation> occupations = Set.of(
        new Occupation(ac_g1_th1_t1, date1, Slot.f08t10, ra),
        new Occupation(ac_g1_prog2_t1, date1, Slot.f10t12, rc));
    var timetable = new Timetable("id", awarded_courses, rooms, locations, dates_all, dates_off, Slot.values(), occupations);

    var timetable_constraint = new TimetableConstraint(timetable, true);
    var solution_occupations = timetable_constraint.solve();
    assertTrue(solution_occupations.isEmpty());

    var violations = timetable_constraint.detectViolations();
    assertEquals(
        Set.of(new Violation("a_teacher_cannot_move_location_half_a_day", Set.of())),
        violations);
  }

  @Test
  public void feasible_two_teachers() {
    var date1 = new Date(2023, JULY, 20);
    var g1 = new Group("g1");
    var g2 = new Group("g2");
    var t1 = new Teacher("t1", date1);
    var t2 = new Teacher("t2", date1);
    var th1 = new Course("th1", Duration.ofHours(2));
    var th2 = new Course("th2", Duration.ofHours(2));
    var prog2 = new Course("prog2", Duration.ofHours(2));
    var prog3 = new Course("prog3", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g1_th2_t1 = new AwardedCourse(th2, g1, t1);
    var ac_g2_prog2_t2 = new AwardedCourse(prog2, g2, t2);
    var ac_g2_prog3_t2 = new AwardedCourse(prog3, g2, t2);
    var awarded_courses = new AwardedCourse[]{ac_g1_th1_t1, ac_g1_th2_t1, ac_g2_prog2_t2, ac_g2_prog3_t2};
    var ra = new Room("a");
    var rd = new Room("d");
    var rooms = new Room[]{ra, rd};
    var locations = new Location[]{
        new Location("l1", Set.of(ra)),
        new Location("l2", Set.of(rd)),
    };

    var dates_all = new Date[]{date1};
    var dates_off = new Date[]{};
    Set<Occupation> occupations = Set.of(
        new Occupation(ac_g1_th1_t1, date1, Slot.f08t10, ra),
        new Occupation(ac_g1_th2_t1, date1, Slot.f10t12, ra),
        new Occupation(ac_g2_prog2_t2, date1, Slot.f08t10, rd));
    var timetable = new Timetable("id", awarded_courses, rooms, locations, dates_all, dates_off, Slot.values(), occupations);

    var timetable_constraint = new TimetableConstraint(timetable, true);
    var solution_occupations = timetable_constraint.solve();
    assertEquals(
        Set.of(
            TimetableConstraint.occupationFrom("occupation[ac:[c:th1][g:g1][t:t1]][d:jul20][r:a][s:f08t10]", timetable),
            TimetableConstraint.occupationFrom("occupation[ac:[c:th2][g:g1][t:t1]][d:jul20][r:a][s:f10t12]", timetable),
            TimetableConstraint.occupationFrom("occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:d][s:f08t10]", timetable),
            TimetableConstraint.occupationFrom("occupation[ac:[c:prog3][g:g2][t:t2]][d:jul20][r:d][s:f10t12]", timetable)),
        solution_occupations);
  }

  @Test
  public void unfeasible_two_teachers() {
    var date1 = new Date(2023, JULY, 20);
    var g1 = new Group("g1");
    var g2 = new Group("g2");
    var t1 = new Teacher("t1", date1);
    var t2 = new Teacher("t2", date1);
    var th1 = new Course("th1", Duration.ofHours(2));
    var th2 = new Course("th2", Duration.ofHours(2));
    var prog2 = new Course("prog2", Duration.ofHours(2));
    var prog3 = new Course("prog3", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g1_th2_t1 = new AwardedCourse(th2, g1, t1);
    var ac_g2_prog2_t2 = new AwardedCourse(prog2, g2, t2);
    var ac_g2_prog3_t2 = new AwardedCourse(prog3, g2, t2);
    var awarded_courses = new AwardedCourse[]{ac_g1_th1_t1, ac_g1_th2_t1, ac_g2_prog2_t2, ac_g2_prog3_t2};
    var ra = new Room("a");
    var rd = new Room("d");
    var rooms = new Room[]{ra, rd};
    var locations = new Location[]{
        new Location("l1", Set.of(ra)),
        new Location("l2", Set.of(rd)),
    };

    var dates_all = new Date[]{date1};
    var dates_off = new Date[]{};
    Set<Occupation> occupations = Set.of(
        new Occupation(ac_g1_th1_t1, date1, Slot.f08t10, ra),
        new Occupation(ac_g1_th2_t1, date1, Slot.f10t12, rd), // cannot move from A to D the same morning!
        new Occupation(ac_g2_prog2_t2, date1, Slot.f08t10, rd));
    var timetable = new Timetable("id", awarded_courses, rooms, locations, dates_all, dates_off, Slot.values(), occupations);

    var timetable_constraint = new TimetableConstraint(timetable, true);
    var solution_occupations = timetable_constraint.solve();
    assertTrue(solution_occupations.isEmpty());

    var violations = timetable_constraint.detectViolations();
    assertEquals(
        Set.of(new Violation("a_teacher_cannot_move_location_half_a_day", Set.of())),
        violations);
  }
}