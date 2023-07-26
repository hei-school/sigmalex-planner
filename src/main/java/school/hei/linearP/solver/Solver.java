package school.hei.linearP.solver;

import school.hei.linearP.LP;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

public abstract class Solver {
  public Solution solve(LP lp) {
    var normalizedLpSets = lp.normalize().toArray(new NormalizedLP[0]);
    if (normalizedLpSets.length != 1) {
      throw new RuntimeException("TODO");
    }
    return solveNormalized(normalizedLpSets[0]);
  }

  protected abstract Solution solveNormalized(NormalizedLP lp);
}
