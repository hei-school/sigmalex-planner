package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import java.util.Set;

public final class True extends Constraint {

  True() {
  }

  public static final NormalizedConstraint TRUE =
      new NormalizedConstraint(new NormalizedLE(0)); // 0 <= 1

  @Override
  public DisjunctivePolytopes normalize() {
    return DisjunctivePolytopes.of(Polytope.of(TRUE));
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
