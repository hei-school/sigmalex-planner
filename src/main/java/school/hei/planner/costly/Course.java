package school.hei.planner.costly;

import java.time.Duration;
import java.util.Objects;

public record Course(String name, Duration duration) implements Costly<Course> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Course costly() {
    return this;
  }

  public Course withDuration(Duration duration) {
    return new Course(name, duration);
  }

  @Override
  public boolean equals(Object o) {
    //TODO(re-enable-duration-equality)
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return Objects.equals(name, course.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
