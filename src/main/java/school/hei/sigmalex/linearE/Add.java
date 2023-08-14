package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.AddIE;
import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.addie;

public record Add(LinearE le1, LinearE le2) implements LinearE {

  @Override
  public NormalizedLE normalize(SubstitutionContext substitutionContext) {
    var normalizedLe1 = le1.normalize(substitutionContext);
    var normalizedLe2 = le2.normalize(substitutionContext);
    var weightedV1 = normalizedLe1.weightedV();
    var weightedV2 = normalizedLe2.weightedV();

    var weightedV = new HashMap<Variable, InstantiableE>();
    weightedV1.forEach((v1, e1) -> weightedV.put(v1, new AddIE(e1, (weightedV2.getOrDefault(v1, ZERO)))));
    weightedV2.forEach((v2, e2) -> {
      if (!weightedV1.containsKey(v2)) {
        weightedV.put(v2, e2);
      }
    });

    return new NormalizedLE(weightedV, addie(normalizedLe1.e(), normalizedLe2.e())).substituteAll(substitutionContext);
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
        le1.variables().stream(),
        le2.variables().stream()
    ).collect(toSet());
  }
}
