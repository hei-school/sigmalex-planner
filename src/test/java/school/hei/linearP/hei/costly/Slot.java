package school.hei.linearP.hei.costly;

import java.time.Duration;

public class Slot extends CostlyBounderValue {

  public static final Duration DURATION = Duration.ofHours(2);
  private static final double DEFAULT_COST_WEIGHT = 10;
  private final String name;
  private final double cost;

  private Slot(String name, double cost) {
    this.name = name;
    this.cost = cost;
  }

  public static Slot from8To10() {
    return new Slot("f08t10", 1);
  }

  public static Slot from10To12() {
    return new Slot("f10t12", 2);
  }

  public static Slot from13To15() {
    return new Slot("f13t15", 100);
  }

  public static Slot from15To17() {
    return new Slot("f13t15", 200);
  }

  public static Slot[] values() {
    return new Slot[]{from8To10(), from10To12(), from13To15(), from15To17()};
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
