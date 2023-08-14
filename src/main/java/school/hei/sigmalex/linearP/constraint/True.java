package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.Set;

public final class True extends Constraint {

  public static final NormalizedConstraint TRUE =
      new NormalizedConstraint(new NormalizedLE(0)); // 0 <= 1

  True() {
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return DisjunctivePolytopes.of(Polytope.of(TRUE));
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
