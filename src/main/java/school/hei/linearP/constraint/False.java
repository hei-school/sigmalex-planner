package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

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
