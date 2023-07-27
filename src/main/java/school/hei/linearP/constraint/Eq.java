package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class Eq extends BiLeConstraint {

  public Eq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
  }

  public Eq(Variable v1, Variable v2) {
    super(v1, v2);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return new And(
        name,
        new Leq(name, le1, le2),
        new Leq(name, le2, le1))
        .normalize();
  }
}
