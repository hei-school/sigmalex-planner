package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class VariadicAnd extends Constraint {
  private final Constraint[] constraints;

  public VariadicAnd(String name, Constraint... constraints) {
    super(name);
    this.constraints = constraints;
  }

  public VariadicAnd(Constraint... constraints) {
    super(null);
    this.constraints = constraints;
  }

  @Override
  public Set<NormalizedConstraint> normalize() {
    return Arrays
        .stream(constraints)
        .flatMap(constraint -> constraint.normalize().stream())
        .collect(toSet());
  }

  @Override
  public Set<Variable> variables() {
    return Arrays.stream(constraints)
        .flatMap(constraint -> constraint.variables().stream())
        .collect(toSet());
  }
}
