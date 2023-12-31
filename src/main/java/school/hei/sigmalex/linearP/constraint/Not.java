package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.Set;

import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public final class Not extends Constraint {

  private final Constraint constraint;

  Not(Constraint constraint) {
    this.constraint = constraint;
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return (switch (constraint) {
      case False f -> TRUE;
      case True t -> FALSE;
      case Not not -> not.constraint;
      case NormalizedConstraint norm -> not(norm.normalize(substitutionContext));
      case And and -> or(and.constraints.stream()
          .map(Constraint::not)
          .toArray(Constraint[]::new));
      case Or or -> and(or.constraints.stream()
          .map(Constraint::not)
          .toArray(Constraint[]::new));
      case Leq leq -> le(leq.le2, leq.le1);
      case Le le -> not(le.normalize(substitutionContext));
      case ForallConstraint forall -> not(forall.normalize());
    }).normalize(substitutionContext);
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
