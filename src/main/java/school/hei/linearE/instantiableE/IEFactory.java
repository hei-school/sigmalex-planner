package school.hei.linearE.instantiableE;

import static school.hei.linearE.instantiableE.Constant.ZERO;

public class IEFactory {
  public static AddIE addie(InstantiableE e1, InstantiableE e2) {
    return new AddIE(e1, e2);
  }

  public static AddIE addie(InstantiableE... ieArray) {
    AddIE nested = new AddIE(ZERO, ieArray[0]);
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

  public static MultIE multie(InstantiableE e, double c) {
    return new MultIE(e, new Constant(c));
  }
}
