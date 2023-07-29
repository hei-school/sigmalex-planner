package school.hei.linearP.constraint;

import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class And extends BiConstraint {

  And(String name, Constraint constraint1, Constraint constraint2) {
    super(name, constraint1, constraint2);
  }

  @Override
  public DisjunctivePolytopes normalize() {
    if (constraint1.equals(TRUE)) {
      return constraint2.normalize();
    }
    if (constraint2.equals(TRUE)) {
      return constraint1.normalize();
    }
    if (constraint1.equals(FALSE) || constraint2.equals(FALSE)) {
      return FALSE.normalize();
    }

    DisjunctivePolytopes res = DisjunctivePolytopes.of();
    // Illustration: ({a}|{b}) & ({c}|{d}) is normalized to: {a&c} | {a&d} | {b&c} | {b&d}
    for (Polytope polytopeFromConstraint1 : constraint1.normalize().polytopes()) {
      for (Polytope polytopeConstraint2 : constraint2.normalize().polytopes()) {
        res.add(new Polytope(Stream.concat(
                polytopeFromConstraint1.constraints().stream(),
                polytopeConstraint2.constraints().stream())
            .collect(toSet())));
      }
    }
    return res;
  }
}
