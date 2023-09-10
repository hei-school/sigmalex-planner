package school.hei.sigmalex.linearP.constraint;

import lombok.SneakyThrows;
import school.hei.exception.NotImplemented;
import school.hei.sigmalex.concurrency.Workers;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
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

  private static DisjunctivePolytopes andPolytopes(
      DisjunctivePolytopes normalized1, DisjunctivePolytopes normalized2) {
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

  @SneakyThrows
  private static Optional<Polytope> toPolytopeOpt(
      List<Constraint> constraints, SubstitutionContext substitutionContext) {
    if (constraints.isEmpty()) {
      return Optional.empty();
    }
    if (constraints.size() == 1) {
      return toPolytopeOpt(constraints.get(0), substitutionContext);
    }

    return Workers.submit(() -> constraints.parallelStream()
            .map(constraint -> toPolytopeOpt(constraint, substitutionContext))
            .reduce(
                Optional.of(Polytope.of()),
                (acc, current) -> acc.isEmpty() || current.isEmpty()
                    ? Optional.empty()
                    : Optional.of(acc.get().add(current.get()))))
        .get();
  }

  private static Optional<Polytope> toPolytopeOpt(
      Constraint constraint, SubstitutionContext substitutionContext) {
    BiFunction<Constraint, SubstitutionContext, Optional<Polytope>> doIt = (lambdaCtr, lambdaCtx) -> {
      var polytopes = new HashSet<>(lambdaCtr.normalize(lambdaCtx).polytopes());
      return polytopes.size() != 1
          ? Optional.empty()
          : Optional.of(polytopes.toArray(Polytope[]::new)[0]);
    };
    return switch (constraint) {
      case Or or -> Optional.empty();
      case Not not -> Optional.empty(); // can be optimized, but empty is sound

      case False aFalse -> throw new NotImplemented();
      case NormalizedConstraint normalizedConstraint -> doIt.apply(constraint, substitutionContext);
      case True aTrue -> throw new NotImplemented();
      case Le le -> doIt.apply(constraint, substitutionContext);
      case Leq leq -> doIt.apply(constraint, substitutionContext);

      case ForallConstraint forallConstraint -> doIt.apply(constraint, substitutionContext);
      case And and -> toPolytopeOpt(and.constraints, substitutionContext);
    };
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
    var polytopeOpt = toPolytopeOpt(constraints, substitutionContext);

    return polytopeOpt.isEmpty()
        ? distributeEachOther(constraints.stream()
        .map(constraint -> constraint.normalize(substitutionContext))
        .collect(toSet()))
        : DisjunctivePolytopes.of(polytopeOpt.get());
  }

  @Override
  public Set<Variable> variables() {
    return constraints.stream()
        .flatMap(constraint -> constraint.variables().stream())
        .collect(toSet());
  }
}
