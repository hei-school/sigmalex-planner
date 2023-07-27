package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Sub;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class Leq extends BiLeConstraint {

  public Leq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
  }

  public Leq(LinearE le1, LinearE le2) {
    super(le1, le2);
  }

  public Leq(LinearE le, double c) {
    super(le, c);
  }

  public Leq(Variable v, double c) {
    super(v, c);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(new NormalizedConstraint(name, new Sub(le1, le2).normalize())));
  }
}
