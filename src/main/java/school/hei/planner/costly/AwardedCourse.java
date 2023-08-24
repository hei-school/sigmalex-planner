package school.hei.planner.costly;

import java.time.Duration;

public record AwardedCourse(Course course, Group group, Teacher teacher) implements Costly<AwardedCourse> {

  public long durationInHours() {
    return course.duration().toHours();
  }

  public boolean equalsExceptDuration(AwardedCourse that) {
    return group.equals(that.group) && teacher.equals(that.teacher) && course.name().equals(that.course.name());
  }

  public AwardedCourse withDurationInHours(long hours) {
    return new AwardedCourse(course.withDuration(Duration.ofHours(hours)), group, teacher);
  }

  @Override
  public AwardedCourse costly() {
    return this;
  }

  @Override
  public String toString() {
    return String.format("[c:%s][g:%s][t:%s]", course, group, teacher);
  }

  public AwardedCourse withCourse(Course course) {
    return new AwardedCourse(course, group, teacher);
  }
}
