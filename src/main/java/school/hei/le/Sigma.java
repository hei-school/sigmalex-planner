package school.hei.lp.le;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public record Sigma(LinearExpression le, Bound bound) implements LinearExpression {

  public Sigma() {
    this(new Mono(0.), new Bound());
  }

  public record Bound(ZVariable k, int kMin, int kMax) {
    public Bound() {
      this(new ZVariable("", List.of()), 0, -1);
    }
  }

  @Override
  public Normalized normalize() {
    var normalizedLeToSigma = le.normalize();
    Variable k = bound.k();
    return IntStream.range(bound.kMin(), bound().kMax() + 1)
        .mapToObj(kI -> (LinearExpression) new Mono(kI))
        .reduce((k1, k2) -> new Add(
            substitute(k, ((Mono) k1).c(), normalizedLeToSigma),
            substitute(k, ((Mono) k2).c(), normalizedLeToSigma)))
        .orElse(new Mono(0.))
        .normalize();
  }

  private Normalized substitute(Variable k, double kValue, Normalized normalized) {
    var weightedV = normalized.weightedV();
    var substitutedWeightedV = new HashMap<>(weightedV);

    if (weightedV.containsKey(k)) {
      substitutedWeightedV.put(k, kValue);
    }

    weightedV.forEach((v, c) -> {
      if (v.getBoundedTo().contains(k)) {
        var boundedToWithoutK = new ArrayList<>(v.getBoundedTo());
        boundedToWithoutK.remove(k);
        substitutedWeightedV.put(v.toNew(v.getName() + "_" + k, boundedToWithoutK), c);
        substitutedWeightedV.remove(v);
      }
    });

    return new Normalized(substitutedWeightedV, normalized.c());
  }
}
