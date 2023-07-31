package school.hei.linearP.hei.costly;

public class Group extends CostlyBounderValue {

  private final String name;
  private final double cost;

  private static final double DEFAULT_COST_WEIGHT = 1;

  private Group(String name, double cost) {
    this.name = name;
    this.cost = cost;
  }

  public static Group g1() {
    return new Group("g1", 1);
  }

  public static Group g2() {
    return new Group("g2", 2);
  }


  public static Group[] values() {
    return new Group[]{g1(), g2()};
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
