package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class Not extends Constraint {

  private final Constraint constraint;

  Not(Constraint constraint) {
    this.constraint = constraint;
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return (switch (constraint) {
      case False f -> TRUE;
      case Not not -> not.constraint;
      case NormalizedConstraint norm -> negDisjOfConj(norm.normalize());
      case VariadicAnd variadicAnd -> negDisjOfConj(variadicAnd.normalize());
      case VariadicOr variadicOr -> negDisjOfConj(variadicOr.normalize());
      case And and -> or(not(and.constraint1), not(and.constraint2));
      case Or or -> and(not(or.constraint1), not(or.constraint2));
      case Leq leq -> le(leq.le2, leq.le1);

      // following constructs are reduced into above ones, hence are unreachable/useless
      // TODO: rm them from sum-type and create isolated factories for them
      case True t -> FALSE;
      case Eq eq -> and(le(eq.le1, eq.le2), le(eq.le2, eq.le1));
      case Le le -> negDisjOfConj(le.normalize());
    }).normalize();
  }

  private VariadicAnd negDisjOfConj(
      Set<Set<NormalizedConstraint>> disjunctionsOfConjunctions) {
    return new VariadicAnd(
        name,
        disjunctionsOfConjunctions.stream()
            .map(this::negCong)
            .toArray(VariadicOr[]::new));
  }

  private VariadicOr negCong(Set<NormalizedConstraint> conjunctionOfConstraints) {
    return new VariadicOr(
        name,
        conjunctionOfConstraints.stream()
            .map(constraint -> new NormalizedConstraint(constraint.le().not()))
            .toArray(NormalizedConstraint[]::new));
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
