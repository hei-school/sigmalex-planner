package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public final class And extends BiConstraint {

  And(Constraint constraint1, Constraint constraint2) {
    super(constraint1, constraint2);
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    if (constraint1.equals(TRUE)) {
      return constraint2.normalize(substitutionContext);
    }
    if (constraint2.equals(TRUE)) {
      return constraint1.normalize(substitutionContext);
    }
    if (constraint1.equals(FALSE) || constraint2.equals(FALSE)) {
      return FALSE.normalize();
    }

    DisjunctivePolytopes res = DisjunctivePolytopes.of();
    // Illustration: ({a}|{b}) & ({c}|{d}) is normalized to: {a&c} | {a&d} | {b&c} | {b&d}
    for (Polytope polytopeFromConstraint1 : constraint1.normalize(substitutionContext).polytopes()) {
      for (Polytope polytopeConstraint2 : constraint2.normalize(substitutionContext).polytopes()) {
        res.add(new Polytope(Stream.concat(
                polytopeFromConstraint1.constraints().stream(),
                polytopeConstraint2.constraints().stream())
            .collect(toSet())));
      }
    }
    return res;
  }
}
