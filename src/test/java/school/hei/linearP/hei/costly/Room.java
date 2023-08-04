package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public record Room(String name) implements BounderValue<Room> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Room costly() {
    return this;
  }
}
