package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public sealed abstract class Constraint
    permits True, False, And, VariadicAnd, Or, Geq, Leq, Eq, NormalizedConstraint {
  protected final String name;

  public Constraint() {
    this.name = null;
  }

  public Constraint(String name) {
    this.name = name;
  }

  public abstract Set<Set<NormalizedConstraint>> normalize(); // set of set due to Or

  public abstract Set<Variable> variables();

  public String name() {
    return name;
  }
}
