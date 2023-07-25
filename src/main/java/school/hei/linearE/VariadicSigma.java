package school.hei.linearE;

import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

import static school.hei.linearE.Sigma.SigmaBound;

public record VariadicSigma(LinearE le, SigmaBound... sigmaBounds) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    Sigma compoundSigma = new Sigma(le, sigmaBounds[0]);
    for (int i = 1; i < sigmaBounds().length; i++) {
      compoundSigma = new Sigma(compoundSigma, sigmaBounds[i]);
    }
    return compoundSigma.normalize();
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
