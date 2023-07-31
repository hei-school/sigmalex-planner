package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public record Constant(double c) implements InstantiableE, BounderValue {
  public static final Constant ZERO = new Constant(0.);
  public static final Constant ONE = new Constant(1.);

  @Override
  public InstantiableE toArithmeticValue() {
    return this;
  }

  @Override
  public double simplify() {
    return c;
  }

  @Override
  public InstantiableE instantiate(Bounder bounder, BounderValue bounderValue)
      throws ArithmeticConversionException {
    return this;
  }

  @Override
  public String toString() {
    return ((int) c + 0.) == c ? ((int) c + "") : (c + "");
  }
}
