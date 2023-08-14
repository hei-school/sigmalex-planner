package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearE.LEFactory;

import static school.hei.sigmalex.linearE.LEFactory.add;

public final class Le extends BiLeConstraint {

  public static double DEFAULT_EPSILON = 0.001;
  private final double epsilon;

  Le(LinearE le1, LinearE le2, double epsilon) {
    super(le1, le2);
    this.epsilon = epsilon;
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return new Leq(LEFactory.add(le1, epsilon), le2).normalize();
  }
}
