package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

import java.time.Duration;

public record Slot(String name, double cost) implements BounderValue<Slot> {

  private static final double DEFAULT_COST_WEIGHT = 10;
  public static Duration DURATION;
  public static int SLOTS_IN_A_DAY;

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
