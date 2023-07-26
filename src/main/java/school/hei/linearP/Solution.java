package school.hei.linearP;

import school.hei.linearE.instantiableE.Variable;

import java.util.Map;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public record Solution(double optimalObjective, Map<Variable, Double> optimalVariables) {

  public static final Solution UNFEASIBLE = new Solution(NaN, Map.of());

  public boolean isEmpty() {
    return isNaN(optimalObjective);
  }
}
