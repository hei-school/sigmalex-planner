package school.hei.linearP.solver;

import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;
import school.hei.linearP.LP;

public abstract class Solver {
  public Solution solve(LP lp) {
    return solveNormalized(lp.normalize());
  }

  protected abstract Solution solveNormalized(NormalizedLP lp);
}
