package school.hei.linearP.constraint;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public final class And extends Constraint {
  private final Constraint constraint1;
  private final Constraint constraint2;

  public And(String name, Constraint constraint1, Constraint constraint2) {
    super(name);
    this.constraint1 = constraint1;
    this.constraint2 = constraint2;
  }

  @Override
  public Set<NormalizedConstraint> normalize() {
    return Stream
        .concat(constraint1.normalize().stream(), constraint2.normalize().stream())
        .collect(toSet());
  }
}
