package school.hei.sigmalex.linearP.solver;

import lombok.extern.slf4j.Slf4j;
import school.hei.sigmalex.linearP.MILP;
import school.hei.sigmalex.linearP.NormalizedMILP;
import school.hei.sigmalex.linearP.Solution;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;

@Slf4j
public abstract class Solver {

  private int totalMilp;
  private int solvedMilp = 0;
  private int printedSolvedMilp = 0;

  public Solution solve(MILP milp) {
    log.info("Disjunctive polytopes normalization... ");
    var normalizedLp = milp.normify();
    log.info("done.");

    totalMilp = normalizedLp.size();
    log.info("Nb of MILP to solve: " + totalMilp + "...");
    return normalizedLp.stream()
        .map(this::solve)
        .peek(solution -> solvedMilp++)
        .peek(this::printProgress)
        .filter(not(Solution::isEmpty))
        .max(comparing(Solution::optimalObjective))
        .orElse(Solution.UNFEASIBLE);
  }

  private void printProgress(Solution solution) {
    if (solvedMilp - printedSolvedMilp > 500) {
      log.info(String.format("Solved MILP: %d / %d...%n", solvedMilp, totalMilp));
      printedSolvedMilp = solvedMilp;
    }
    if (solvedMilp == totalMilp) {
      log.info(String.format("... all %d MILP solved.%n", totalMilp));
    }
  }

  protected abstract Solution solve(NormalizedMILP lp);
}
