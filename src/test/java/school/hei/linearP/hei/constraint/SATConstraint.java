package school.hei.linearP.hei.constraint;

import school.hei.linearP.constraint.Constraint;

import java.util.List;

public interface SATConstraint {
  Constraint constraint();

  /**
   * Completely verify whether constraint is SAT.
   *
   * @return Complete list of violations that renders the constraint UNSAT.
   * No violation means the constraint is SAT.
   */
  default List<Violation> sat() {
    throw new RuntimeException("TODO");
  }
}
