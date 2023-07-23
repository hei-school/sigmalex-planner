package school.hei.linearE.instantiableE;

public record MultE(InstantiableE e1, InstantiableE e2) implements InstantiableE {

  @Override
  public InstantiableE simplify() {
    return e1 instanceof Constant && e2 instanceof Constant
        ? new Constant(((Constant) e1).c() * ((Constant) e2).c())
        : new MultE(e1.simplify(), e2.simplify()).simplify();
  }
}
