package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

import java.time.Duration;

public record Course(String name, Duration duration) implements BounderValue<Course> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Course costly() {
    return this;
  }
}
