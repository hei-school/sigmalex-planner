package school.hei.linearP.constraint;

import java.util.Set;

public sealed abstract class Constraint permits And, Eq, Geq, Leq, NormalizedConstraint, VariadicAnd {
  protected String name;

  public Constraint(String name) {
    this.name = name;
  }

  public abstract Set<NormalizedConstraint> normalize();
}
