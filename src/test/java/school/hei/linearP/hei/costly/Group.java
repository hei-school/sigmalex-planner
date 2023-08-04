package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public record Group(String name) implements BounderValue<Group> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Group costly() {
    return this;
  }
}
