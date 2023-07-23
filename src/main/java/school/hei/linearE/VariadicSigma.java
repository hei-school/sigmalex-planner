package school.hei.linearE;

import static school.hei.linearE.Sigma.SigmaBound;

public record VariadicSigma(LinearE le, SigmaBound... sigmaBounds) implements LinearE {
  @Override
  public Normalized normalize() {
    Sigma compoundSigma = new Sigma(le, sigmaBounds[0]);
    for (int i = 1; i < sigmaBounds().length; i++) {
      compoundSigma = new Sigma(compoundSigma, sigmaBounds[i]);
    }
    return compoundSigma.normalize();
  }
}
