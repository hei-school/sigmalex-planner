package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Sub;

import java.util.Set;

public final class Geq extends Constraint {

  private final LinearE le1;
  private final LinearE le2;

  public Geq(String name, LinearE le1, LinearE le2) {
    super(name);
    this.le1 = le1;
    this.le2 = le2;
  }

  @Override
  public Set<NormalizedConstraint> normalize() {
    return Set.of(new NormalizedConstraint(name, new Sub(le2, le1).normalize()));
  }
}
