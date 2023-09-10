package school.hei.sigmalex.linearP.constraint;

import lombok.SneakyThrows;
import school.hei.exception.NotImplemented;
import school.hei.sigmalex.concurrency.Workers;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public final class And extends ListConstraint {

  public And(List<Constraint> constraints) {
    super(sanitize(constraints));
  }

  private static DisjunctivePolytopes distributeEachOther(Set<DisjunctivePolytopes> normalizedList) {
    if (normalizedList.size() == 1) {
      return normalizedList.stream().toList().get(0);
    }

    DisjunctivePolytopes res = DisjunctivePolytopes.of();
    var normalizedArray = normalizedList.toArray(DisjunctivePolytopes[]::new);
    for (int i = 0; i < normalizedArray.length; i++) {
      for (int j = i + 1; j < normalizedArray.length; j++) {
        res = andPolytopes(res, andPolytopes(normalizedArray[i], normalizedArray[j]));
      }
    }
    return res;
  }

  private static DisjunctivePolytopes andPolytopes(DisjunctivePolytopes normalized1, DisjunctivePolytopes normalized2) {
    if (normalized1.isEmpty()) {
      return normalized2;
    }
    if (normalized2.isEmpty()) {
      return normalized1;
    }

    DisjunctivePolytopes res = DisjunctivePolytopes.of();
    // Illustration: ({a}|{b}) & ({c}|{d}) is normalized to: {a&c} | {a&d} | {b&c} | {b&d}
    for (var polytopeFromConstraint1 : normalized1.polytopes()) {
      for (var polytopeConstraint2 : normalized2.polytopes()) {
        res.add(new Polytope(Stream.concat(
                polytopeFromConstraint1.constraints().stream(),
                polytopeConstraint2.constraints().stream())
            .collect(toSet())));
      }
    }
    return res;
  }

  private static Polytope andPolytopes(Set<Polytope> polytopes) {
    var constraints = new ArrayList<NormalizedConstraint>();
    for (var polytope : polytopes) {
      constraints.addAll(polytope.constraints());
    }
    return Polytope.of(constraints.toArray(NormalizedConstraint[]::new));
  }

  @SneakyThrows
  private static Optional<Set<Polytope>> flatten(
      List<Constraint> constraints, SubstitutionContext substitutionContext) {
    if (constraints.isEmpty()) {
      return Optional.empty();
    }
    if (constraints.size() == 1) {
      var constraint = constraints.get(0);
      return switch (constraint) {
        case Or or -> Optional.empty();
        case Not not -> Optional.empty(); // can be optimized, but empty is sound

        case False aFalse -> throw new NotImplemented();
        case NormalizedConstraint normalizedConstraint -> flatten(constraint, substitutionContext);
        case True aTrue -> throw new NotImplemented();
        case Le le -> flatten(constraint, substitutionContext);
        case Leq leq -> flatten(constraint, substitutionContext);

        case ForallConstraint forallConstraint -> flatten(constraint, substitutionContext);
        case And and -> flatten(and.constraints, substitutionContext);
      };
    }

    return Workers.submit(() -> constraints.parallelStream()
            .map(constraint -> flatten(List.of(constraint), substitutionContext))
            .reduce(
                Optional.of(Set.of()),
                (acc, current) -> acc.isEmpty() || current.isEmpty()
                    ? Optional.empty()
                    : Optional.of(Stream.concat(acc.get().stream(), current.get().stream()).collect(toSet()))))
        .get();
  }

  private static Optional<Set<Polytope>> flatten(Constraint constraint, SubstitutionContext substitutionContext) {
    var polytopes = constraint.normalize(substitutionContext).polytopes().stream().toList();
    if (polytopes.size() != 1) {
      return Optional.empty();
    }
    return Optional.of(Set.of(polytopes.get(0)));
  }

  private static List<Constraint> sanitize(List<Constraint> constraints) {
    if (constraints.size() == 0) {
      return List.of(TRUE);
    }
    if (constraints.contains(FALSE)) {
      return List.of(FALSE);
    }

    var res = new ArrayList<>(constraints);
    res.remove(TRUE);
    return res;
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    Optional<Set<Polytope>> flattenedAndOpt = flatten(constraints, substitutionContext);

    return flattenedAndOpt.isEmpty()
        ? distributeEachOther(constraints.stream()
        .map(constraint -> constraint.normalize(substitutionContext))
        .collect(toSet()))
        : DisjunctivePolytopes.of(andPolytopes(flattenedAndOpt.get()));
  }

  @Override
  public Set<Variable> variables() {
    return constraints.stream()
        .flatMap(constraint -> constraint.variables().stream())
        .collect(toSet());
  }
}
