package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;

import java.util.Set;

import static school.hei.linearE.LEFactory.sub;

public final class Leq extends BiLeConstraint {

  Leq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(new NormalizedConstraint(name, sub(le1, le2).normalize())));
  }
}
