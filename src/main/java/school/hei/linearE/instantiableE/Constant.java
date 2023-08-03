package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.function.Function;

public record Constant<Costly>(double c) implements InstantiableE<Costly>, BounderValue<Costly> {
  public static final Constant ZERO = new Constant(0.);
  public static final Constant ONE = new Constant(1.);

  @Override
  public double simplify() {
    return c;
  }

  @Override
  public InstantiableE<Costly> toQ(Costly costly, Function<Costly, InstantiableE<Costly>> instantiator)
      throws ArithmeticConversionException {
    return this;
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

  @Override
  public Costly costly() {
    return null;
  }
}
