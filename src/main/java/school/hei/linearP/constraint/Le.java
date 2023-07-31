package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import static school.hei.linearE.LEFactory.add;

public final class Le extends BiLeConstraint {

  public static double DEFAULT_EPSILON = 0.001;
  private final double epsilon;

  Le(LinearE le1, LinearE le2, double epsilon) {
    super(le1, le2);
    this.epsilon = epsilon;
  }

  @Override
  public DisjunctivePolytopes normalize() {
    return new Leq(add(le1, epsilon), le2).normalize();
  }
}
