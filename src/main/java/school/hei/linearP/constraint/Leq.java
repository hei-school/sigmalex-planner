package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Sub;

import java.util.Set;

public final class Leq extends BiLeConstraint {

  Leq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(new NormalizedConstraint(name, new Sub(le1, le2).normalize())));
  }
}
