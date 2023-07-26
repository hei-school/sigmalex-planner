package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Variable;

import java.util.HashSet;
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
  public Set<Set<NormalizedConstraint>> normalize() {
    Set<Set<NormalizedConstraint>> res = new HashSet<>();

    // Illustration: ({a}|{b}) & ({c}|{d}) is normalized to: {a&c} | {a&d} | {b&c} | {b&d}
    for (Set<NormalizedConstraint> setFromConstraint1 : constraint1.normalize()) {
      for (Set<NormalizedConstraint> setFromConstraint2 : constraint2.normalize()) {
        res.add(Stream.concat(
                setFromConstraint1.stream(),
                setFromConstraint2.stream())
            .collect(toSet()));
      }
    }
    return res;
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
            constraint1.variables().stream(),
            constraint2.variables().stream())
        .collect(toSet());
  }
}
