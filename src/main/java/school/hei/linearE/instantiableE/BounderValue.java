package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public interface BounderValue {
  InstantiableE toArithmeticValue() throws ArithmeticConversionException;
}
