package school.hei.linearP.hei.costly;

public record AwardedCourse(Course course, Group group, Teacher teacher) implements Costly<AwardedCourse> {

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
