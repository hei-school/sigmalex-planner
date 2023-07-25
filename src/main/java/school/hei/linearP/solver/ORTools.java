package school.hei.linearP.solver;

import com.google.ortools.linearsolver.MPVariable;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SigmaZ;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

import java.util.HashMap;
import java.util.Map;

import static com.google.ortools.Loader.loadNativeLibraries;
import static com.google.ortools.linearsolver.MPSolver.createSolver;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 * <a href="https://developers.google.com/optimization/introduction/java">Google OR Tools</a>
 */
public class ORTools extends Solver {
  @Override
  protected Solution solveNormalized(NormalizedLP lp) {
    loadNativeLibraries();
    var solver = createSolver("SCIP"); // https://www.scipopt.org/

    var lpvToMpv = new HashMap<Variable, MPVariable>();
    lp.variables().forEach(v -> lpvToMpv.put(v, switch (v) {
      case Q q -> solver.makeNumVar(NEGATIVE_INFINITY, POSITIVE_INFINITY, q.getName());
      case Z z -> solver.makeIntVar(NEGATIVE_INFINITY, POSITIVE_INFINITY, z.getName());
      case SigmaZ sigmaZ -> throw new UnsupportedOperationException();
    }));

    var objective = solver.objective();
    lp.objective()
        .weightedV()
        .forEach((v, c) -> objective.setCoefficient(lpvToMpv.get(v), c.instantiate()));
    switch (lp.optimizationType()) {
      case min -> objective.setMinimization();
      case max -> objective.setMaximization();
      default -> throw new UnsupportedOperationException();
    }

    lp.constraints().forEach(constraint -> {
      var mpc = solver.makeConstraint(
          NEGATIVE_INFINITY,
          -1 * constraint.le().e().instantiate(),
          constraint.name() == null ? "" : constraint.name());
      constraint.variables().forEach(v -> mpc.setCoefficient(lpvToMpv.get(v), constraint.weight(v)));
    });

    int swigValue = solver.solve().swigValue();
    if (swigValue != 0) {
      return new Solution(NaN, Map.of());
    }

    var optimalVariables = new HashMap<Variable, Double>();
    lpvToMpv.forEach((lpv, mpv) -> optimalVariables.put(lpv, mpv.solutionValue()));
    return new Solution(objective.value(), optimalVariables);
  }
}