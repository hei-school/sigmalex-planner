package school.hei.linearP.constraint;

import java.util.Set;

public sealed abstract class Constraint permits And, Eq, Geq, Leq, NormalizedConstraint, VariadicAnd {
  protected final String name;

  public Constraint() {
    this.name = null;
  }

  public Constraint(String name) {
    this.name = name;
  }

  public abstract Set<NormalizedConstraint> normalize();
}
