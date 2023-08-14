package school.hei.sigmalex.linearE.instantiableE;

import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;

public record Constant<Costly>(double c) implements InstantiableE<Costly>, BounderValue<Costly> {
  public static final Constant ZERO = new Constant<>(0.);
  public static final Constant ONE = new Constant<>(1.);

  @Override
  public double simplify() {
    return c;
  }

  @Override
  public InstantiableE<Costly> toQ(
      Costly costly, SubstitutionContext substitutionContext, Instantiator<Costly> instantiator)
      throws ArithmeticConversionException {
    return this;
  }

  @Override
  public InstantiableE<Costly> instantiate(
      Bounder<Costly> bounder, BounderValue<Costly> bounderValue, SubstitutionContext substitutionContext)
      throws ArithmeticConversionException {
    return this;
  }

  @Override
  public String toString() {
    return ((int) c + 0.) == c ? ((int) c + "") : (c + "");
  }

  @Override
  public Costly costly() {
    return null;
  }
}
