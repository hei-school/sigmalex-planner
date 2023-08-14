package school.hei.planner.constraint;

import school.hei.sigmalex.linearP.constraint.Constraint;

import java.util.Set;

/**
 * A ViolatorConstraint is a constraint that:
 * (a) can be blamed as definite violator that UNSAT its enclosing problem,
 * (b) or can be definitely cleared as it would never UNSAT its enclosing problem,
 * (c) or may be blamed or cleared as we don't know whether it would UNSAT its enclosing problem.
 */
public interface ViolatorConstraint {
  Constraint constraint();

  ThreeValuedLogic isViolator();

  default Set<String> violationRemedySuggestions() {
    return Set.of();
  }
}
