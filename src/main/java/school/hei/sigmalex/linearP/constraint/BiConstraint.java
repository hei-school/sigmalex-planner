package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public abstract sealed class BiConstraint extends Constraint permits And, Or {

  protected final Constraint constraint1;
  protected final Constraint constraint2;

  public BiConstraint(Constraint constraint1, Constraint constraint2) {
    this.constraint1 = constraint1;
    this.constraint2 = constraint2;
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
            constraint1.variables().stream(),
            constraint2.variables().stream())
        .collect(toSet());
  }
}
