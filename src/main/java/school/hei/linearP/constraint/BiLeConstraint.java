package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Mono;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract sealed class BiLeConstraint extends Constraint permits Eq, Geq, Le, Leq {
  protected final LinearE le1;
  protected final LinearE le2;

  public BiLeConstraint(String name, LinearE le1, LinearE le2) {
    super(name);
    this.le1 = le1;
    this.le2 = le2;
  }

  public BiLeConstraint(String name, LinearE le1, double le2) {
    this(name, le1, new Mono(le2));
  }

  public BiLeConstraint(LinearE le1, LinearE le2) {
    this(null, le1, le2);
  }

  public BiLeConstraint(LinearE le1, double le2) {
    this(null, le1, le2);
  }

  public BiLeConstraint(Variable v, double c) {
    this(null, new Mono(v), new Mono(c));
  }

  public BiLeConstraint(double c1, double c2) {
    this(null, new Mono(c1), new Mono(c2));
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
            le1.variables().stream(),
            le2.variables().stream())
        .collect(toSet());
  }
}
