package school.hei.planner.costly;

import java.util.Arrays;
import java.util.List;

public record Teacher(String name, List<Date> availabilities) implements Costly<Teacher> {

  public Teacher(String name, Date... availabilities) {
    this(name, Arrays.stream(availabilities).toList());
  }

  public boolean isAvailableOn(Date date) {
    return availabilities.contains(date);
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
