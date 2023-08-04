package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public record AwardedCourse(Course course, Group group, Teacher teacher) implements BounderValue<AwardedCourse> {

  public double durationInHours() {
    return course.duration().toHours();
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
