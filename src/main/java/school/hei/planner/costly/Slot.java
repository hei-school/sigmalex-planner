package school.hei.planner.costly;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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

  public static Set<Slot> valuesSet() {
    return Arrays.stream(Slot.values()).collect(toSet());
  }

  public static Set<Slot> morning() {
    return Set.of(f08t10, f10t12);
  }

  public static Set<Slot> afternoon() {
    return Set.of(f13t15, f15t17);
  }

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
