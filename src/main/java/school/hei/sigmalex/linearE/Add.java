package school.hei.sigmalex.linearE;

import lombok.SneakyThrows;
import school.hei.sigmalex.concurrency.Workers;
import school.hei.sigmalex.linearE.instantiableE.AddIE;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.addie;

public record Add(List<LinearE> leList) implements LinearE {

  @Override
  public NormalizedLE normalize(SubstitutionContext substitutionContext) {
    return normalizeDichotomically(substitutionContext);
  }

  @SneakyThrows
  private NormalizedLE normalizeDichotomically(SubstitutionContext substitutionContext) {
    var leListSize = leList.size();
    if (leListSize == 0) {
      return new NormalizedLE(0);
    }
    if (leListSize == 1) {
      return leList.get(0).normalize(substitutionContext);
    }
    if (leListSize == 2) {
      return normalize(leList.get(0), leList.get(1), substitutionContext);
    }

    var futureOperand1 = Workers.submit(() ->
        new Add(leList.subList(0, leListSize / 2)).normalize(substitutionContext));
    var futureOperand2 = Workers.submit(() ->
        new Add(leList.subList(leListSize / 2, leListSize)).normalize(substitutionContext));
    return normalize(futureOperand1.get(), futureOperand2.get());
  }

  private NormalizedLE normalize(NormalizedLE normalized1, NormalizedLE normalized2) {
    var weightedV = new HashMap<>(normalized1.weightedV());
    normalized2.weightedV().forEach((v, e) -> {
      if (weightedV.containsKey(v)) {
        weightedV.put(v, new Constant<>(weightedV.get(v).simplify() + e.simplify()));
      } else {
        weightedV.put(v, e);
      }
    });

    var e = new Constant<>(normalized1.e().simplify() + normalized2.e().simplify());

    return new NormalizedLE(weightedV, e);
  }

  private NormalizedLE normalize(LinearE le1, LinearE le2, SubstitutionContext substitutionContext) {
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
    return leList.stream().flatMap(le -> le.variables().stream()).collect(toSet());
  }
}
