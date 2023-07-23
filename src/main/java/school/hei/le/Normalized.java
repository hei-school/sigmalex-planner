package school.hei.le;

import java.util.Map;

public record Normalized(Map<Variable, Double> weightedV, double c) implements LinearExpression {

  @Override
  public Normalized normalize() {
    return this;
  }
}
