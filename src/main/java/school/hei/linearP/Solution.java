package school.hei.linearP;

public class Solution {

  //TODO: an abstraction leak of lpSolver while waiting for a text parser from LPSolver
  private final String lpSolverSolution;

  public Solution(String lpSolverSolution) {
    this.lpSolverSolution = lpSolverSolution.trim();
  }

  @Override
  public String toString() {
    return lpSolverSolution;
  }
}
