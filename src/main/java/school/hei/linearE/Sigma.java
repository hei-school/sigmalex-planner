package school.hei.linearE;

import school.hei.linearE.instantiableE.AddE;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.MultE;
import school.hei.linearE.instantiableE.SigmaZ;
import school.hei.linearE.instantiableE.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public record Sigma(LinearE le, SigmaBound sigmaBound) implements LinearE {

  public record SigmaBound(SigmaZ k, int kMin, int kMax) {
  }

  @Override
  public Normalized normalize() {
    var normalizedLeToSigma = le.normalize();
    Variable k = sigmaBound.k();
    return IntStream.range(sigmaBound.kMin(), sigmaBound().kMax() + 1)
        .mapToObj(kI -> new Mono(kI).normalize())
        .reduce(
            new Mono(0.).normalize(),
            (k1, k2) -> new Add(
                k1,
                substitute(k, k2.e(), normalizedLeToSigma)).normalize());
  }

  private Normalized substitute(Variable k, InstantiableE kValue, Normalized normalized) {
    var weightedV = normalized.weightedV();
    var substitutedWeightedV = new HashMap<>(weightedV);
    weightedV.forEach((v, c) -> {
      if (v.getBoundedTo().contains(k)) {
        var boundedToWithoutK = new ArrayList<>(v.getBoundedTo());
        boundedToWithoutK.remove(k);
        substitutedWeightedV.put(v.toNew(v.getName() + "_" + kValue, boundedToWithoutK), c);
        substitutedWeightedV.remove(v);
      }
    });

    var newE = normalized.e();
    if (weightedV.containsKey(k)) {
      newE = new AddE(newE, new MultE(kValue, weightedV.get(k)));
      substitutedWeightedV.remove(k);
    }
    return new Normalized(substitutedWeightedV, newE);
  }
}
