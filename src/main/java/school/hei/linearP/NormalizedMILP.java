package school.hei.linearP;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.NormalizedConstraint;

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
