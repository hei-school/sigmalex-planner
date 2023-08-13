package school.hei.linearP.hei.test;

import org.junit.jupiter.api.Test;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.Occupation;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;
import school.hei.linearP.hei.constraint.Violation;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnfeasibleHEITest {

  @Test
  public void sigmalex_the_wise_can_tell_why_a_timetable_is_unfeasible() {
    var g1 = new Group("g1");
    var t1 = new Teacher(
        "t1",
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24));
    var th1 = new Course("th1", Duration.ofHours(6));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var awarded_courses = new AwardedCourse[]{ac_g1_th1_t1};
    var ra = new Room("a");
    var rb = new Room("b");
    var rooms = new Room[]{ra, rb};
    var dates_all = new Date[]{
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23)};
    var dates_off = new Date[]{
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22)};
    Set<Occupation> occupations = Set.of();
    var timetable = new HEITimetable(awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);

    var timetable_constraint = new HEITimetableConstraint(timetable);
    var solution_occupations = timetable_constraint.solve();
    assertTrue(solution_occupations.isEmpty());

    var violations = timetable_constraint.detectViolations();
    assertEquals(
        Set.of(
            new Violation(
                "only_one_slot_max_per_course_per_day",
                Set.of("Increase school days", "Increase teacher availabilities", "Decrease course hours")),
            new Violation(
                "finish_course_hours_with_available_teachers",
                Set.of("Increase school days", "Increase teacher availabilities", "Decrease course hours"))),
        violations);
  }
}
