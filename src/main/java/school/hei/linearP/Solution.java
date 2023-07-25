package school.hei.linearP;

import school.hei.linearE.instantiableE.Variable;

import java.util.Map;

import static java.lang.Double.isNaN;

public record Solution(double optimalObjective, Map<Variable, Double> optimalVariables) {
  public boolean isEmpty() {
    return isNaN(optimalObjective);
  }
}
