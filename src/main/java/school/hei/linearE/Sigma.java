package school.hei.linearE;

import school.hei.linearE.instantiableE.AddE;
import school.hei.linearE.instantiableE.ArithmeticConversion;
import school.hei.linearE.instantiableE.Bounder;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.MultE;
import school.hei.linearE.instantiableE.SigmaZ;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public record Sigma(LinearE le, SigmaBound sigmaBound) implements LinearE {

  public record SigmaBound(Bounder bounder, BounderValue... values) {
    public SigmaBound(SigmaZ k, int kMin, int kMax) {
      this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
    }
  }

  @Override
  public NormalizedLE normalize() {
    var normalizedLeToSigma = le.normalize();
    LinearE summed = new Mono(0);

    var bounderValues = sigmaBound.values();
    for (BounderValue bounderValue : bounderValues) {
      summed = new Add(
          summed,
          substitute(sigmaBound.bounder().variable(), bounderValue, normalizedLeToSigma));
    }
    return summed.normalize();
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }

  private NormalizedLE substitute(Variable k, BounderValue kValue, NormalizedLE normalizedLE) {
    var weightedV = normalizedLE.weightedV();
    var substitutedWeightedV = new HashMap<>(weightedV);
    weightedV.forEach((v, c) -> {
      if (v.getBounders().contains(k)) {
        var boundedToWithoutK = new HashSet<>(v.getBounders());
        boundedToWithoutK.remove(k);
        substitutedWeightedV.put(v.toNew(v.getName() + "_" + kValue, boundedToWithoutK), c);
        substitutedWeightedV.remove(v);
      }
    });

    var newE = normalizedLE.e();
    if (weightedV.containsKey(k)) {
      try {
        newE = new AddE(newE, new MultE(kValue.toArithmeticValue(), weightedV.get(k)));
      } catch (ArithmeticConversion e) {
        throw new RuntimeException(e);
      }
      substitutedWeightedV.remove(k);
    }
    return new NormalizedLE(substitutedWeightedV, newE);
  }
}
