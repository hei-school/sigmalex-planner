package school.hei.linearP.constraint;

import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class Or extends BiConstraint {

  Or(String name, Constraint constraint1, Constraint constraint2) {
    super(name, constraint1, constraint2);
  }

  @Override
  public DisjunctivePolytopes normalize() {
    if (constraint1.equals(FALSE)) {
      return constraint2.normalize();
    }
    if (constraint2.equals(FALSE)) {
      return constraint1.normalize();
    }
    if (constraint1.equals(TRUE) || constraint2.equals(TRUE)) {
      return TRUE.normalize();
    }

    return new DisjunctivePolytopes(Stream.concat(
            constraint1.normalize().polytopes().stream(),
            constraint2.normalize().polytopes().stream())
        .collect(toSet()));
  }
}
