package school.hei.linearE.instantiableE;

public class InstantiableEFactory {
  public static AddIE addie(InstantiableE e1, InstantiableE e2) {
    return new AddIE(e1, e2);
  }

  public static MultIE multie(InstantiableE e1, InstantiableE e2) {
    return new MultIE(e1, e2);
  }

  public static MultIE multie(double c, InstantiableE e) {
    return new MultIE(new Constant(c), e);
  }

  public static MultIE multie(InstantiableE e, double c) {
    return new MultIE(e, new Constant(c));
  }
}
