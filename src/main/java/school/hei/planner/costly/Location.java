package school.hei.planner.costly;

import java.util.Set;

public record Location(String name, Set<Room> rooms) implements Costly<Location> {
  @Override
  public Location costly() {
    return this;
  }
}
