package school.hei.linearP.solver;

import com.google.ortools.linearsolver.MPVariable;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SigmaZ;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

import java.util.HashMap;

import static com.google.ortools.Loader.loadNativeLibraries;
import static com.google.ortools.linearsolver.MPSolver.createSolver;

/**
 * <a href="https://developers.google.com/optimization/introduction/java">Google OR Tools</a>
 */
public class ORTools extends Solver {

  private static final double V_MIN_VALUE = -1_000_000;
  private static final double CONSTRAINT_MIN_VALUE = 1_000_000 * V_MIN_VALUE;
  private static final double V_MAX_VALUE = -1 * V_MIN_VALUE;

  @Override
  protected Solution solveNormalized(NormalizedLP lp) {
    loadNativeLibraries();
    var solver =
        // GLOP: Google Linear Optimization Package
        createSolver("GLOP");

    var lpvToMpv = new HashMap<Variable, MPVariable>();
    lp.variables().forEach(v -> lpvToMpv.put(v, switch (v) {
      case Q q -> solver.makeNumVar(V_MIN_VALUE, V_MAX_VALUE, q.getName());
      case Z z -> solver.makeIntVar(V_MIN_VALUE, V_MAX_VALUE, z.getName());
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
          CONSTRAINT_MIN_VALUE,
          -1 * constraint.le().e().instantiate(),
          constraint.name() == null ? "" : constraint.name());
      constraint.variables().forEach(v -> mpc.setCoefficient(lpvToMpv.get(v), constraint.weight(v)));
    });

    solver.solve();

    var optimalVariables = new HashMap<Variable, Double>();
    lpvToMpv.forEach((lpv, mpv) -> optimalVariables.put(lpv, mpv.solutionValue()));
    return new Solution(objective.value(), optimalVariables);
  }
}
