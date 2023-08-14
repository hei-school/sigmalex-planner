package school.hei.sigmalex.linearE.instantiableE;

public class IEFactory {
  public static AddIE addie(InstantiableE e1, InstantiableE e2) {
    return new AddIE(e1, e2);
  }

  public static AddIE addie(InstantiableE... ieArray) {
    AddIE nested = new AddIE(Constant.ZERO, ieArray[0]);
    for (int i = 1; i < ieArray.length; i++) {
      nested = new AddIE(nested, ieArray[i]);
    }
    return nested;
  }

  public static MultIE multie(InstantiableE e1, InstantiableE e2) {
    return new MultIE(e1, e2);
  }

  public static MultIE multie(double c, InstantiableE e) {
    return new MultIE(new Constant(c), e);
  }

  public static MultIE multie(double c1, double c2) {
    return new MultIE(new Constant(c1), new Constant(c2));
  }

  public static MultIE multie(InstantiableE e, double c) {
    return new MultIE(e, new Constant(c));
  }
}
