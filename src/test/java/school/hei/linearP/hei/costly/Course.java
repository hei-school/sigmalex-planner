package school.hei.linearP.hei.costly;

import lombok.Value;

import java.time.Duration;

@Value
public class Course extends Costly<Course> {
  String name;
  Duration duration;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Course costly() {
    return this;
  }
}
