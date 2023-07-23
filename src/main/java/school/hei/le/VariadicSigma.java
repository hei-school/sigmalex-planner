package school.hei.le;

public record VariadicSigma(LinearExpression le, Sigma.Bound... bounds) implements LinearExpression {
  @Override
  public Normalized normalize() {
    Sigma compoundSigma = new Sigma(le, bounds[0]);
    for (int i = 1; i < bounds().length; i++) {
      compoundSigma = new Sigma(compoundSigma, bounds[i]);
    }
    return compoundSigma.normalize();
  }
}
