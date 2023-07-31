package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public abstract class CostlyBounderValue implements Costly, BounderValue {

  @Override
  public InstantiableE toArithmeticValue() throws ArithmeticConversionException {
    return new Constant(cost());
  }
}
