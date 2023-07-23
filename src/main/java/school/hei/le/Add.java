package school.hei.le;

import java.util.HashMap;

public record Add(LinearExpression le1, LinearExpression le2) implements LinearExpression {
  @Override
  public Normalized normalize() {
    var normalizedLe1 = le1.normalize();
    var normalizedLe2 = le2.normalize();
    var weightedV1 = normalizedLe1.weightedV();
    var weightedV2 = normalizedLe2.weightedV();

    var weightedV = new HashMap<Variable, Double>();
    weightedV1.forEach((v, c) -> weightedV.put(v, c + weightedV2.getOrDefault(v, 0.)));
    weightedV2.forEach((v, c) -> {
      if (!weightedV1.containsKey(v)) {
        weightedV.put(v, c);
      }
    });

    return new Normalized(weightedV, normalizedLe1.c() + normalizedLe2.c());
  }
}
