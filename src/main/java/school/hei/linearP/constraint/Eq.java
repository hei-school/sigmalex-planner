package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;

import java.util.Set;

public final class Eq extends BiLeConstraint {

  public Eq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
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
