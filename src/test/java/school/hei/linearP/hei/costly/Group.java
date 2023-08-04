package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public record Group(String name) implements BounderValue<Group> {

  public static Group g1() {
    return new Group("g1");
  }

  public static Group g2() {
    return new Group("g2");
  }


  public static Group[] values() {
    return new Group[]{g1(), g2()};
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Group costly() {
    return this;
  }
}
