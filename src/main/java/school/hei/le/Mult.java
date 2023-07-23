package school.hei.le;

import java.util.HashMap;

public record Mult(double c, LinearExpression le) implements LinearExpression {
  @Override
  public Normalized normalize() {
    var normalizedLeToMult = le.normalize();
    var weightedV = new HashMap<Variable, Double>();
    normalizedLeToMult.weightedV().forEach((v, cToMult) -> weightedV.put(v, c * cToMult));
    return new Normalized(weightedV, c * normalizedLeToMult.c());
  }
}
