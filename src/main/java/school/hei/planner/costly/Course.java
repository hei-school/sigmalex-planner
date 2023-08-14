package school.hei.planner.costly;

import java.time.Duration;

public record Course(String name, Duration duration) implements Costly<Course> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Course costly() {
    return this;
  }
}
