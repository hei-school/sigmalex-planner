package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import static school.hei.linearE.LEFactory.sub;

public final class Leq extends BiLeConstraint {

  Leq(LinearE le1, LinearE le2) {
    super(le1, le2);
  }

  @Override
  public DisjunctivePolytopes normalize() {
    return DisjunctivePolytopes.of(Polytope.of(new NormalizedConstraint(sub(le1, le2).normalize())));
  }
}
