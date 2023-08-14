package school.hei.sigmalex.linearE.instantiableE;

import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;

public sealed interface InstantiableE<Costly> permits AddIE, Constant, InstantiableV, MultIE {
  double simplify();

  InstantiableE<?> instantiate(
      Bounder<Costly> bounder, BounderValue<Costly> bounderValue, SubstitutionContext substitutionContext)
      throws ArithmeticConversionException;
}
