package school.hei.linearP.hei.costly;

import java.time.Duration;

public class Course extends CostlyBounderValue {

  private final String name;
  private final double cost;

  private static final double DEFAULT_COST_WEIGHT = 10;

  public static final Duration DURATION = Duration.ofHours(8);

  private Course(String name, double cost) {
    this.name = name;
    this.cost = cost;
  }

  public static Course th1() {
    return new Course("th1", 1);
  }

  public static Course prog2() {
    return new Course("prog2", 2);
  }


  public static Course[] values() {
    return new Course[]{th1(), prog2()};
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
