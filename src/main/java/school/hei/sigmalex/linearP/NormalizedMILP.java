package school.hei.sigmalex.linearP;

import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.HashSet;
import java.util.Set;

public record NormalizedMILP(
    String name,
    OptimizationType optimizationType,
    NormalizedLE objective,
    Set<NormalizedConstraint> constraints) {
  public Set<Variable> variables() {
    var res = new HashSet<Variable>();
    constraints.forEach(constraint -> res.addAll(constraint.variables()));
    return res;
  }
}
