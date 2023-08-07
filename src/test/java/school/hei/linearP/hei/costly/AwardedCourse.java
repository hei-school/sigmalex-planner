package school.hei.linearP.hei.costly;

import lombok.Value;

@Value
public class AwardedCourse extends Costly<AwardedCourse> {

  Course course;
  Group group;
  Teacher teacher;

  public double durationInHours() {
    return course.getDuration().toHours();
  }

  @Override
  public AwardedCourse costly() {
    return this;
  }

  @Override
  public String toString() {
    return String.format("[c:%s][g:%s][t:%s]", course, group, teacher);
  }
}
