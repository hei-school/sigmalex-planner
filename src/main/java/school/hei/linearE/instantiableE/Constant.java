package school.hei.linearE.instantiableE;

public record Constant(double c) implements InstantiableE {
  public static final Constant ZERO = new Constant(0.);

  @Override
  public InstantiableE simplify() {
    return this;
  }
}
