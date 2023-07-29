package school.hei.linearE;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Variable;

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
          normalizedLeToSigma.substitute(bound.bounder(), bounderValue));
    }
    return summed.normalize();
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
