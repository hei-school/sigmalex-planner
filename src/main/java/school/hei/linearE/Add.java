package school.hei.linearE;

import school.hei.linearE.instantiableE.AddE;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;

import static school.hei.linearE.instantiableE.Constant.ZERO;

public record Add(LinearE le1, LinearE le2) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    var normalizedLe1 = le1.normalize();
    var normalizedLe2 = le2.normalize();
    var weightedV1 = normalizedLe1.weightedV();
    var weightedV2 = normalizedLe2.weightedV();

    var weightedV = new HashMap<Variable, InstantiableE>();
    weightedV1.forEach((v1, e1) -> weightedV.put(v1, new AddE(e1, (weightedV2.getOrDefault(v1, ZERO)))));
    weightedV2.forEach((v2, e2) -> {
      if (!weightedV1.containsKey(v2)) {
        weightedV.put(v2, e2);
      }
    });

    return new NormalizedLE(weightedV, new AddE(normalizedLe1.e(), (normalizedLe2.e())));
  }
}
