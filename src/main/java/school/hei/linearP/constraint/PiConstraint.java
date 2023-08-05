package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.Set;

public final class PiConstraint extends Constraint {

  private final Constraint constraint;
  private final SubstitutionContext substitutionContext;

  public PiConstraint(Constraint constraint, SubstitutionContext substitutionContext) {
    this.constraint = constraint;
    this.substitutionContext = substitutionContext;
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return constraint.normalize(substitutionContext.add(this.substitutionContext));
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
