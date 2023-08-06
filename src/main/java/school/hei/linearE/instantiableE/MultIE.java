package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public record MultIE(InstantiableE e1, InstantiableE e2) implements InstantiableE {
  @Override
  public double simplify() {
    return e1.simplify() * e2.simplify();
  }

  @Override
  public InstantiableE instantiate(Bounder bounder, BounderValue bounderValue, SubstitutionContext substitutionContext)
      throws ArithmeticConversionException {
    return new MultIE(
        e1.instantiate(bounder, bounderValue, substitutionContext),
        e2.instantiate(bounder, bounderValue, substitutionContext));
  }

  @Override
  public String toString() {
    return String.format("(%s * %s)", e1.toString(), e2.toString());
  }
}
