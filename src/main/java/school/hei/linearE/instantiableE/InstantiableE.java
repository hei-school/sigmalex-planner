package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public sealed interface InstantiableE permits AddIE, Constant, InstantiableV, MultIE {
  double simplify();

  InstantiableE instantiate(Bounder bounder, BounderValue bounderValue) throws ArithmeticConversionException;
}
