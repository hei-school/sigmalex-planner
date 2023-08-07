package school.hei.linearP.hei.costly;

import lombok.Value;

import java.time.Duration;

@Value
public class Slot extends Costly<Slot> {

  private static final double DEFAULT_COST_WEIGHT = 10;
  public static Duration DURATION;
  public static int SLOTS_IN_A_DAY;
  String name;
  double cost;

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
