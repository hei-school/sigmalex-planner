package school.hei.linearP.constraint;

import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.constraint.NormalizedConstraint;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class VariadicAnd extends Constraint {
  private final Constraint[] constraints;

  public VariadicAnd(String name, Constraint... constraints) {
    super(name);
    this.constraints = constraints;
  }

  @Override
  public Set<NormalizedConstraint> normalize() {
    return Arrays
        .stream(constraints)
        .flatMap(constraint -> constraint.normalize().stream())
        .collect(toSet());
  }
}
