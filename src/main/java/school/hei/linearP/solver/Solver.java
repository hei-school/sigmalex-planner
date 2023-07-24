package school.hei.linearP.solver;

import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

public interface Solver {
  Solution solve(NormalizedLP lp);
}
