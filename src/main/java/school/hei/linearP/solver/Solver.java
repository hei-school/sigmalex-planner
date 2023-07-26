package school.hei.linearP.solver;

import school.hei.linearP.LP;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;
import static school.hei.linearP.Solution.UNFEASIBLE;

public abstract class Solver {
  public Solution solve(LP lp) {
    return lp.normalize().stream()
        .map(this::solveNormalized)
        .filter(not(Solution::isEmpty))
        .max(comparing(Solution::optimalObjective))
        .orElse(UNFEASIBLE);
  }

  protected abstract Solution solveNormalized(NormalizedLP lp);
}
