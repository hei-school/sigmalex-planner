package school.hei.linearP.hei.constraint;

import school.hei.linearP.MILP;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.solver.ORTools;

import java.util.Set;

import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearP.OptimizationType.min;

public interface SATConstraint {
  Constraint constraint();

  /**
   * Completely verify whether constraint is SAT.
   *
   * @return Complete list of violations that renders the constraint UNSAT.
   * No violation means the constraint is SAT.
   */
  default Set<Violation> sat() {
    // Refine this default method per implemented constraint for easier violation detection
    var milp = new MILP(min, mono(0), constraint());
    var solution = new ORTools().solve(milp);
    return solution.isEmpty()
        ? Set.of(new Violation(getClass().getName()))
        : Set.of();
  }
}
