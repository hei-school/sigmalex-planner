package school.hei.linearP.hei.costly;

import lombok.Value;

@Value
public class Group extends Costly<Group> {
  String name;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Group costly() {
    return this;
  }
}
