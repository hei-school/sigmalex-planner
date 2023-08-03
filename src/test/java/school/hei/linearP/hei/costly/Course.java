package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.time.Duration;
import java.util.function.Function;

public class Course extends CostlyBounderValue<Course> {

  private static final double DEFAULT_COST_WEIGHT = 10;
  private final String name;
  private final double cost;
  private final Duration duration;

  private Course(String name, double cost, Duration duration) {
    this.name = name;
    this.cost = cost;
    this.duration = duration;
  }

  public static Course th1() {
    return new Course("th1", 1, Duration.ofHours(6));
  }

  public static Course prog2() {
    return new Course("prog2", 2, Duration.ofHours(8));
  }

  public static Course sem1() {
    return new Course("sem1", 3, Duration.ofHours(2));
  }

  public static Course[] values() {
    return new Course[]{th1(), prog2(), sem1()};
  }

  @Override
  public double cost() {
    return DEFAULT_COST_WEIGHT * cost;
  }

  public double durationInHours() {
    return duration.toHours();
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Course costly() {
    return this;
  }

  @Override
  public InstantiableE<Course> toQ(Course costly, Function<Course, InstantiableE<Course>> instantiator)
      throws ArithmeticConversionException {
    return instantiator.apply(costly);
  }
}
