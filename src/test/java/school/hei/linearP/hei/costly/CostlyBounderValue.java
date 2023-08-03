package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.function.Function;

public abstract class CostlyBounderValue<T> implements Costly, BounderValue<T> {

  @Override
  public InstantiableE<T> toQ(T t, Function<T, InstantiableE<T>> instantiator) throws ArithmeticConversionException {
    return new Constant<>(cost());
  }
}
