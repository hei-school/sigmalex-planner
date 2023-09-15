package school.hei.sigmalex.linearP;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.Constraint;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearE.LEFactory.mono;

public record MILP(
    String name,
    OptimizationType optimizationType,
    LinearE objective,
    Set<Constraint> constraints) {

  public MILP(OptimizationType optimizationType,
              LinearE objective,
              Set<Constraint> constraints) {
    this(null, optimizationType, objective, constraints);
  }

  public MILP(String name,
              OptimizationType optimizationType,
              LinearE objective,
              Constraint... constraints) {
    this(name, optimizationType, objective, Set.of(constraints));
  }

  public MILP(OptimizationType optimizationType,
              LinearE objective,
              Constraint... constraints) {
    this(null, optimizationType, objective, Set.of(constraints));
  }

  public MILP(OptimizationType optimizationType,
              Variable v,
              Constraint... constraints) {
    this(null, optimizationType, mono(v), Set.of(constraints));
  }

  public Set<NormalizedMILP> normify() {
    var disjunctivePolytopes = Constraint.and(constraints.toArray(new Constraint[0]))
        .normalize();
    return disjunctivePolytopes.polytopes().stream()
        .map(polytope -> new NormalizedMILP(
            name,
            optimizationType,
            objective.subnormify(),
            polytope.constraints().stream()
                .map(NormalizedConstraint::simplify)
                .collect(toSet())))
        .collect(toSet());
  }
}
