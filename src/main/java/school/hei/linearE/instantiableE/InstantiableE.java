package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public sealed interface InstantiableE<Costly> permits AddIE, Constant, InstantiableV, MultIE {
  double simplify();

  InstantiableE<Costly> instantiate(Bounder<Costly> bounder, BounderValue<Costly> bounderValue)
      throws ArithmeticConversionException;
}
