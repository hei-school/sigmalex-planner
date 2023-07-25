package school.hei.linearP;

import school.hei.linearE.instantiableE.Variable;

import java.util.Map;

public record Solution(double optimalObjective, Map<Variable, Double> optimalVariables) {
}
