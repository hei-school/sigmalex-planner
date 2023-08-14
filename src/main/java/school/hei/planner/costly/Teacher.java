package school.hei.planner.costly;

import java.util.Arrays;

public record Teacher(String name, Date... availabilities) implements Costly<Teacher> {

  public boolean isAvailableOn(Date date) {
    return Arrays.asList(availabilities).contains(date);
  }

  @Override
  public Teacher costly() {
    return this;
  }

  @Override
  public String toString() {
    return name;
  }
}
