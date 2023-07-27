package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class Not extends Constraint {

  private final Constraint constraint;

  public Not(Constraint constraint) {
    this.constraint = constraint;
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return (switch (constraint) {
      case False f -> TRUE;
      case Not not -> not.constraint;
      case NormalizedConstraint normalizedConstraint -> not(normalizedConstraint);
      case VariadicAnd variadicAnd -> not(variadicAnd);
      case VariadicOr variadicOr -> not(variadicOr);
      case And and -> new Or(new Not(and.constraint1), new Not(and.constraint2));
      case Or or -> new And(new Not(or.constraint1), new Not(or.constraint2));
      case Leq leq -> new Le(leq.le2, leq.le1);

      // following constructs are reduced into above ones, hence are unreachable/useless
      // TODO: rm them from sum-type and create isolated factories for them
      case True t -> FALSE;
      case Eq eq -> new And(new Le(eq.le1, eq.le2), new Le(eq.le2, eq.le1));
      case Geq geq -> new Le(geq.le2, geq.le1);
      case Le le -> negateDisjunctionsOfConjunctions(le.normalize());
    }).normalize();
  }

  private VariadicAnd not(Constraint constraint) {
    return negateDisjunctionsOfConjunctions(constraint.normalize());
  }

  private VariadicAnd negateDisjunctionsOfConjunctions(
      Set<Set<NormalizedConstraint>> disjunctionsOfConjunctions) {
    return new VariadicAnd(
        name,
        disjunctionsOfConjunctions.stream()
            .map(this::negateConjunctions)
            .toArray(VariadicOr[]::new));
  }

  private VariadicOr negateConjunctions(Set<NormalizedConstraint> conjunctionOfConstraints) {
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
