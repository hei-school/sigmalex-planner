package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract sealed class BiLeConstraint extends Constraint permits Le, Leq {
  protected final LinearE le1;
  protected final LinearE le2;

  public BiLeConstraint(LinearE le1, LinearE le2) {
    this.le1 = le1;
    this.le2 = le2;
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
            le1.variables().stream(),
            le2.variables().stream())
        .collect(toSet());
  }
}
