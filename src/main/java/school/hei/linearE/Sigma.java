package school.hei.linearE;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

import static school.hei.linearE.LEFactory.add;
import static school.hei.linearE.LEFactory.mono;

public record Sigma(LinearE le, Bound bound) implements LinearE {

  @Override
  public NormalizedLE normalize() {
    var normalizedLeToSigma = le.normalize();
    LinearE summed = mono(0);

    var bounderValues = bound.values();
    for (BounderValue bounderValue : bounderValues) {
      summed = add(
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
