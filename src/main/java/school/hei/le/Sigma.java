package school.hei.le;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public record Sigma(LinearExpression le, Bound bound) implements LinearExpression {

  public record Bound(ZVariable k, int kMin, int kMax) {
  }

  @Override
  public Normalized normalize() {
    var normalizedLeToSigma = le.normalize();
    Variable k = bound.k();
    return IntStream.range(bound.kMin(), bound().kMax() + 1)
        .mapToObj(kI -> new Mono(kI).normalize())
        .reduce(
            new Mono(0.).normalize(),
            (k1, k2) -> new Add(
                k1,
                substitute(k, k2.c(), normalizedLeToSigma)).normalize());
  }

  private Normalized substitute(Variable k, double kValue, Normalized normalized) {
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

    var newC = normalized.c();
    if (weightedV.containsKey(k)) {
      newC += kValue * weightedV.get(k);
      substitutedWeightedV.remove(k);
    }
    return new Normalized(substitutedWeightedV, newC);
  }
}
