package school.hei.linearP.solver;

import school.hei.linearP.constraint.LP;
import school.hei.linearP.Solution;

public interface Solver {
  Solution solve(LP lp);
}
