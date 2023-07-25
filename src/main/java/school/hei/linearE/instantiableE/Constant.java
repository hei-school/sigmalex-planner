package school.hei.linearE.instantiableE;

public record Constant(double c) implements InstantiableE, BounderValue {
  public static final Constant ZERO = new Constant(0.);
  public static final Constant ONE = new Constant(1.);

  @Override
  public InstantiableE toArithmeticValue() {
    return this;
  }

  @Override
  public InstantiableE simplify() {
    return this;
  }

  @Override
  public String toString() {
    return ((int) c + 0.) == c ? ((int) c + "") : (c + "");
  }
}
