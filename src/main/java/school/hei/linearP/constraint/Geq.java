package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Mono;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public final class Geq extends Constraint {

  private final LinearE le1;
  private final LinearE le2;

  public Geq(String name, LinearE le1, LinearE le2) {
    super(name);
    this.le1 = le1;
    this.le2 = le2;
  }

  public Geq(LinearE le1, LinearE le2) {
    super(null);
    this.le1 = le1;
    this.le2 = le2;
  }

  public Geq(String name, LinearE le, double c) {
    this(name, le, new Mono(c));
  }

  public Geq(Variable v, double c) {
    this(null, new Mono(v), c);
  }

  public Geq(double c1, double c2) {
    this(null, new Mono(c1), c2);
  }

  public Geq(LinearE le, double c) {
    this(null, le, c);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return new Leq(name, le2, le1).normalize();
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
            le1.variables().stream(),
            le1.variables().stream())
        .collect(toSet());
  }
}
