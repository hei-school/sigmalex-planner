package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import java.util.Set;

import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class Not extends Constraint {

  private final Constraint constraint;

  Not(Constraint constraint) {
    this.constraint = constraint;
  }

  @Override
  public DisjunctivePolytopes normalize() {
    return (switch (constraint) {
      case False f -> TRUE;
      case True t -> FALSE;
      case Not not -> not.constraint;
      case NormalizedConstraint norm -> not(norm.normalize());
      case And and -> or(not(and.constraint1), not(and.constraint2));
      case Or or -> and(not(or.constraint1), not(or.constraint2));
      case Leq leq -> le(leq.le2, leq.le1);
      case Le le -> not(le.normalize());
      case PiConstraint pi -> not(pi.normalize());
    }).normalize();
  }

  private Constraint not(DisjunctivePolytopes disjunctivePolytopes) {
    return and(
        disjunctivePolytopes.polytopes().stream()
            .map(this::negCong)
            .toArray(Constraint[]::new));
  }

  private Constraint negCong(Polytope polytope) {
    return or(
        polytope.constraints().stream()
            .map(constraint -> new NormalizedConstraint(constraint.le().not()))
            .toArray(NormalizedConstraint[]::new));
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
