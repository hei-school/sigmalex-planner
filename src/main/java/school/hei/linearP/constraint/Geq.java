package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class Geq extends BiLeConstraint {

  public Geq(String name, LinearE le1, LinearE le2) {
    super(name, le1, le2);
  }

  public Geq(Variable v, double c) {
    super(v, c);
  }

  public Geq(double c1, double c2) {
    super(c1, c2);
  }

  public Geq(LinearE le, double c) {
    super(le, c);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return new Leq(name, le2, le1).normalize();
  }
}
