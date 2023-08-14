package school.hei.planner.costly;

public record Group(String name) implements Costly<Group> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Group costly() {
    return this;
  }
}
