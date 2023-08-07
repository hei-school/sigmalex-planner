package school.hei.linearP.hei.costly;

import lombok.Value;

import java.util.Arrays;

@Value
public class Teacher extends Costly<Teacher> {

  String name;
  Date[] availabilities;

  public Teacher(String name, Date... availabilities) {
    this.name = name;
    this.availabilities = availabilities;
  }

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
