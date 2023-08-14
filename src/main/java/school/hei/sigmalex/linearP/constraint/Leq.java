package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import static school.hei.sigmalex.linearE.LEFactory.sub;

public final class Leq extends BiLeConstraint {

  Leq(LinearE le1, LinearE le2) {
    super(le1, le2);
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return DisjunctivePolytopes.of(Polytope.of(new NormalizedConstraint(sub(le1, le2).normalize(substitutionContext))));
  }
}
