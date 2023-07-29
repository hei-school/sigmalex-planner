package school.hei.linearE;

import school.hei.linearE.instantiableE.AddIE;
import school.hei.linearE.instantiableE.ArithmeticConversionException;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.MultIE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public record Sigma(LinearE le, Bound bound) implements LinearE {

  @Override
  public NormalizedLE normalize() {
    var normalizedLeToSigma = le.normalize();
    LinearE summed = new Mono(0);

    var bounderValues = bound.values();
    for (BounderValue bounderValue : bounderValues) {
      summed = new Add(
          summed,
          substitute(bound.bounder().variable(), bounderValue, normalizedLeToSigma));
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
        substitutedWeightedV.put(
            v.toNew(v.getName() + "[" + k.getName() + ":" + kValue + "]", boundedToWithoutK), c);
        substitutedWeightedV.remove(v);
      }
    });

    var newE = normalizedLE.e();
    if (weightedV.containsKey(k)) {
      try {
        newE = new AddIE(newE, new MultIE(kValue.toArithmeticValue(), weightedV.get(k)));
      } catch (ArithmeticConversionException e) {
        throw new RuntimeException(e);
      }
      substitutedWeightedV.remove(k);
    }
    return new NormalizedLE(substitutedWeightedV, newE);
  }
}
