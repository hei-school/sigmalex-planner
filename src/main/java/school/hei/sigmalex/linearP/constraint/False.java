package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.Set;

public final class False extends Constraint {

  public static final NormalizedConstraint FALSE =
      new NormalizedConstraint(new NormalizedLE(1)); // 1 <= 0

  False() {
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return DisjunctivePolytopes.of(Polytope.of(FALSE));
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
