package school.hei.planner.web;

import org.junit.jupiter.api.Disabled;
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
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectMapperTest {

  @Disabled //TODO
  @Test
  public void serialize_then_deserialize_gives_identity() {
    var date1 = new Date(LocalDate.of(2023, JULY, 20));
    var g1 = new Group("g1");
    var t1 = new Teacher("t1", date1);
    var th1 = new Course("th1", Duration.ofHours(2));
    var prog2 = new Course("prog2", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g1_prog2_t1 = new AwardedCourse(prog2, g1, t1);
    var awarded_courses = new AwardedCourse[]{ac_g1_th1_t1, ac_g1_prog2_t1};
    var ra = new Room("a");
    var rooms = new Room[]{ra};
    var dates_all = new Date[]{date1};
    var dates_off = new Date[]{};
    Set<Occupation> occupations = Set.of(
        new Occupation(ac_g1_th1_t1, date1, Slot.f08t10, ra),
        new Occupation(ac_g1_prog2_t1, date1, Slot.f15t17, ra));
    var timetable = new Timetable("id", awarded_courses, rooms, dates_all, dates_off, Slot.values(), occupations);

    var om = new ObjectMapper();
    var serialized = om.writeValueAsString(timetable);
    assertTrue(serialized.contains("awardedCourses"));

    var deserialized = om.readValue(serialized, Timetable.class);
    assertEquals(serialized, om.writeValueAsString(deserialized));
  }
}