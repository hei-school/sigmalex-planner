package school.hei.lp.le;

import school.hei.lp.le.Sigma.Bound;

public record VariadicSigma(LinearExpression le, Bound... bounds) implements LinearExpression {
  @Override
  public Normalized normalize() {
    Sigma compoundSigma = new Sigma();
    for (Bound bound : bounds) {
      compoundSigma = new Sigma(compoundSigma, bound);
    }
    return compoundSigma.normalize();
  }
}
