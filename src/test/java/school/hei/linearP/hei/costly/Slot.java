package school.hei.linearP.hei.costly;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public enum Slot implements Costly<Slot> {
  f08t10("f08t10", 1),
  f10t12("f10t12", 2),
  f13t15("f13t15", 10),
  f15t17("f15t17", 20);

  public static final Duration DURATION = Duration.ofHours(2);
  public static final int SLOTS_IN_A_DAY = 4;
  private static final double DEFAULT_COST_WEIGHT = 10;
  private final String name;
  private final double cost;

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Slot costly() {
    return this;
  }

  public double cost() {
    return DEFAULT_COST_WEIGHT * cost;
  }
}
