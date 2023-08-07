package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public sealed class Costly<T> implements BounderValue<T>
    permits AwardedCourse, Course, Date, Group, Room, Slot, Teacher {
  @Override
  public T costly() {
    return null;
  }
}
