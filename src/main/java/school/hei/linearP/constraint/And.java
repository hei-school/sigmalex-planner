package school.hei.linearP.constraint;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class And extends BiConstraint {

  And(String name, Constraint constraint1, Constraint constraint2) {
    super(name, constraint1, constraint2);
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    if (constraint1.equals(TRUE)) {
      return constraint2.normalize();
    }
    if (constraint2.equals(TRUE)) {
      return constraint1.normalize();
    }
    if (constraint1.equals(FALSE) || constraint2.equals(FALSE)) {
      return FALSE.normalize();
    }

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
}
