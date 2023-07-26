package school.hei.linearP;

import school.hei.linearE.LinearE;
import school.hei.linearE.Mono;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.constraint.VariadicAnd;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record LP(
    String name,
    OptimizationType optimizationType,
    LinearE objective,
    Set<Constraint> constraints) {

  public LP(OptimizationType optimizationType,
            LinearE objective,
            Set<Constraint> constraints) {
    this(null, optimizationType, objective, constraints);
  }

  public LP(String name,
            OptimizationType optimizationType,
            LinearE objective,
            Constraint... constraints) {
    this(name, optimizationType, objective, Set.of(constraints));
  }

  public LP(OptimizationType optimizationType,
            LinearE objective,
            Constraint... constraints) {
    this(null, optimizationType, objective, Set.of(constraints));
  }

  public LP(OptimizationType optimizationType,
            Variable v,
            Constraint... constraints) {
    this(null, optimizationType, new Mono(v), Set.of(constraints));
  }

  public Set<NormalizedLP> normalize() {
    var normalizedConstraintsSets = new VariadicAnd(
        name, constraints.toArray(new Constraint[0]))
        .normalize();
    return normalizedConstraintsSets.stream()
        .map(normalizedConstraintsSet -> new NormalizedLP(
            name,
            optimizationType,
            objective.normalize(),
            normalizedConstraintsSet))
        .collect(toSet());
  }
}
