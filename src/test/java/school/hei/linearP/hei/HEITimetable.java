package school.hei.linearP.hei;

import lombok.Getter;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Costly;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.hei.costly.Teacher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class HEITimetable {
  @Getter
  private final AwardedCourse[] awardedCourses;
  private final Map<String, Course> coursesByName;
  private final Map<String, Group> groupsByName;
  private final Map<String, Teacher> teachersByName;


  @Getter
  private final Room[] rooms;
  private final Map<String, Room> roomsByName;

  @Getter
  private final Date[] datesAll;
  private final Map<String, Date> datesAllByName;

  @Getter
  private final Date[] datesOff;

  @Getter
  private final Slot[] slots;
  private final Map<String, Slot> slotsByName;


  @Getter
  private final Set<Occupation> occupations;

  public HEITimetable(
      AwardedCourse[] awardedCourses,
      Room[] rooms,
      Date[] datesAll,
      Date[] datesOff,
      Slot[] slots,
      Set<Occupation> occupations) {
    this.awardedCourses = awardedCourses;
    this.rooms = rooms;
    this.datesAll = datesAll;
    this.datesOff = datesOff;
    this.slots = slots;
    this.occupations = occupations;

    this.coursesByName = noDuplicateName(courses().stream().collect(groupingBy(Course::toString)));
    this.groupsByName = noDuplicateName(groups().stream().collect(groupingBy(Group::toString)));
    this.teachersByName = noDuplicateName(teachers().stream().collect(groupingBy(Teacher::toString)));
    this.roomsByName = noDuplicateName(Arrays.stream(rooms).collect(groupingBy(Room::toString)));
    this.datesAllByName = noDuplicateName(Arrays.stream(datesAll).collect(groupingBy(Date::toString)));
    this.slotsByName = noDuplicateName(Arrays.stream(slots).collect(groupingBy(Slot::toString)));
  }

  public Set<Course> courses() {
    return Arrays.stream(awardedCourses).map(AwardedCourse::course).collect(toSet());
  }

  public Set<Group> groups() {
    return Arrays.stream(awardedCourses).map(AwardedCourse::group).collect(toSet());
  }

  public Set<Teacher> teachers() {
    return Arrays.stream(awardedCourses).map(AwardedCourse::teacher).collect(toSet());
  }

  private <T extends Costly<?>> Map<String, T> noDuplicateName(Map<String, List<T>> byName) {
    Map<String, T> res = new HashMap<>();
    byName.forEach((name, tList) -> {
      if (tList.size() > 1) {
        throw new RuntimeException("A name should be associated with a costly: name=" + name);
      }
      res.put(name, tList.get(0));
    });
    return res;
  }

  public Course courseByName(String name) {
    return coursesByName.get(name);
  }


  public Group groupByName(String name) {
    return groupsByName.get(name);
  }

  public Teacher teacherByName(String name) {
    return teachersByName.get(name);
  }

  public Date dateByName(String name) {
    return datesAllByName.get(name);
  }


  public Slot slotByName(String name) {
    return slotsByName.get(name);
  }

  public Room roomByName(String name) {
    return roomsByName.get(name);
  }
}
