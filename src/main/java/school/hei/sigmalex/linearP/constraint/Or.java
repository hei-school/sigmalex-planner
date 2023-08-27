package school.hei.sigmalex.linearP.constraint;

import lombok.SneakyThrows;
import school.hei.sigmalex.concurrency.Workers;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public final class Or extends ListConstraint {

  public Or(List<Constraint> constraints) {
    super(sanitize(constraints));
  }

  private static List<Constraint> sanitize(List<Constraint> constraints) {
    if (constraints.size() == 0) {
      return List.of(FALSE);
    }
    if (constraints.contains(TRUE)) {
      return List.of(TRUE);
    }

    var res = new ArrayList<>(constraints);
    res.remove(FALSE);
    return res;
  }

  @SneakyThrows
  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    return flatten(Workers.submit(() -> constraints.parallelStream()
        .map(constraint -> constraint.normalize(substitutionContext))
        .collect(toSet())).get());
  }

  private DisjunctivePolytopes flatten(Set<DisjunctivePolytopes> disjunctivePolytopes) {
    return new DisjunctivePolytopes(disjunctivePolytopes.stream()
        .flatMap(dp -> dp.polytopes().stream())
        .collect(toSet()));
  }
}
