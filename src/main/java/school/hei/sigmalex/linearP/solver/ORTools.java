package school.hei.sigmalex.linearP.solver;

import com.google.ortools.linearsolver.MPVariable;
import school.hei.sigmalex.linearE.instantiableE.B;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearE.instantiableE.Z;
import school.hei.sigmalex.linearP.NormalizedMILP;
import school.hei.sigmalex.linearP.Solution;

import java.util.HashMap;

import static com.google.ortools.Loader.loadNativeLibraries;
import static com.google.ortools.linearsolver.MPSolver.createSolver;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static school.hei.sigmalex.linearP.Solution.UNFEASIBLE;

/**
 * <a href="https://developers.google.com/optimization/introduction/java">Google OR Tools</a>
 */
public class ORTools extends Solver {
  @Override
  protected Solution solve(NormalizedMILP lp) {
    loadNativeLibraries();
    var solver = createSolver("SCIP"); // https://www.scipopt.org/
    //solver.setNumThreads(env.NUM_THREADS);

    var lpvToMpv = new HashMap<Variable, MPVariable>();
    lp.variables().forEach(v -> lpvToMpv.put(v, switch (v) {
      case Q q -> solver.makeNumVar(NEGATIVE_INFINITY, POSITIVE_INFINITY, q.boundedName());
      case Z z -> solver.makeIntVar(NEGATIVE_INFINITY, POSITIVE_INFINITY, z.boundedName());
      case B b -> solver.makeBoolVar(b.boundedName());
      case BounderQ<?> bounderQ -> throw new UnsupportedOperationException();
    }));

    var objective = solver.objective();
    lp.objective()
        .simplify()
        .weightedV()
        .forEach((v, c) -> objective.setCoefficient(lpvToMpv.get(v), c.simplify()));
    switch (lp.optimizationType()) {
      case min -> objective.setMinimization();
      case max -> objective.setMaximization();
      default -> throw new UnsupportedOperationException();
    }

    lp.constraints().forEach(constraint -> {
      var mpc = solver.makeConstraint(
          NEGATIVE_INFINITY,
          -1 * constraint.le().e().simplify(),
          "");
      constraint.variables().forEach(v -> mpc.setCoefficient(lpvToMpv.get(v), constraint.weight(v)));
    });

    var lpString = solver.exportModelAsLpFormat();
    System.out.print(lpString.substring(
        0,
        lpString.indexOf("Minimize") > 0 ? lpString.indexOf("Minimize") : lpString.indexOf("Maximize")));
    int swigValue = solver.solve().swigValue();
    if (swigValue != 0) {
      return UNFEASIBLE;
    }

    var optimalVariables = new HashMap<Variable, Double>();
    lpvToMpv.forEach((lpv, mpv) -> optimalVariables.put(lpv, mpv.solutionValue()));
    return new Solution(objective.value(), optimalVariables);
  }
}
