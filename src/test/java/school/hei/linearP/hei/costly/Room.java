package school.hei.linearP.hei.costly;

import lombok.Value;

@Value
public class Room extends Costly<Room> {
  String name;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Room costly() {
    return this;
  }
}
