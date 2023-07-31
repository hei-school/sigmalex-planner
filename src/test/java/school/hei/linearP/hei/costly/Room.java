package school.hei.linearP.hei.costly;

public class Room extends CostlyBounderValue {

  private final String name;
  private final double cost;

  private static final double DEFAULT_COST_WEIGHT = 0.1;

  private Room(String name, double cost) {
    this.name = name;
    this.cost = cost;
  }

  public static Room a() {
    return new Room("a", 1);
  }

  public static Room b() {
    return new Room("b", 2);
  }


  public static Room[] values() {
    return new Room[]{a(), b()};
  }

  @Override
  public double cost() {
    return DEFAULT_COST_WEIGHT * cost;
  }

  @Override
  public String toString() {
    return name;
  }
}
