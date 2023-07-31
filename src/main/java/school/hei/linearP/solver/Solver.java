package school.hei.linearP.solver;

import school.hei.linearP.LP;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;
import static school.hei.linearP.Solution.UNFEASIBLE;

public abstract class Solver {

  private int totalMilp;
  private int solvedMilp = 0;
  private int printedSolvedMilp = 0;

  public Solution solve(LP lp) {
    System.out.print("Disjunctive polytopes normalization... ");
    var normalizedLp = lp.normify();
    System.out.println("done.");

    totalMilp = normalizedLp.size();
    System.out.println("Nb of MILP to solve: " + totalMilp + "...");
    return normalizedLp.stream()
        .map(this::solveNormalized)
        .peek(solution -> solvedMilp++)
        .peek(this::printProgress)
        .filter(not(Solution::isEmpty))
        .max(comparing(Solution::optimalObjective))
        .orElse(UNFEASIBLE);
  }

  private void printProgress(Solution solution) {
    if (solvedMilp - printedSolvedMilp > 500) {
      System.out.printf("Solved MILP: %d / %d...%n", solvedMilp, totalMilp);
      printedSolvedMilp = solvedMilp;
    }
    if (solvedMilp == totalMilp) {
      System.out.printf("... all %d MILP solved.%n", totalMilp);
    }
  }

  protected abstract Solution solveNormalized(NormalizedLP lp);
}
