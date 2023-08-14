package school.hei.linearP;

import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public record Solution(double optimalObjective, Map<Variable, Double> optimalVariables) {

  public static final Solution UNFEASIBLE = new Solution(NaN, Map.of());

  public boolean isEmpty() {
    return isNaN(optimalObjective);
  }

  public Map<String, Double> optimalNonNullVariablesForUnboundedName(String vName) {
    Map<String, Double> res = new HashMap<>();
    optimalVariables.forEach((v, c) -> {
      if (vName.equals(v.name()) && c != 0) {
        res.put(v.boundedName(), c);
      }
    });
    return res;
  }
}
