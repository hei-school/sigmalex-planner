package school.hei.linearP.constraint;

import school.hei.linearE.Add;
import school.hei.linearE.LinearE;

import java.util.Set;

public final class Le extends BiLeConstraint {

  public static double DEFAULT_EPSILON = 0.001;
  private final double epsilon;

  Le(String name, LinearE le1, LinearE le2, double epsilon) {
    super(name, le1, le2);
    this.epsilon = epsilon;
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return new Leq(name, new Add(le1, epsilon), le2).normalize();
  }
}
