package school.hei.linearP.hei.costly;

public record Room(String name) implements Costly<Room> {
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Room costly() {
    return this;
  }
}
