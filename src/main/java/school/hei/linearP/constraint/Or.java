package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public final class Or extends BiConstraint {

  Or(Constraint constraint1, Constraint constraint2) {
    super(constraint1, constraint2);
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    if (constraint1.equals(FALSE)) {
      return constraint2.normalize(substitutionContext);
    }
    if (constraint2.equals(FALSE)) {
      return constraint1.normalize(substitutionContext);
    }
    if (constraint1.equals(TRUE) || constraint2.equals(TRUE)) {
      return TRUE.normalize(substitutionContext);
    }

    return new DisjunctivePolytopes(Stream.concat(
            constraint1.normalize(substitutionContext).polytopes().stream(),
            constraint2.normalize(substitutionContext).polytopes().stream())
        .collect(toSet()));
  }
}
