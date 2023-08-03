package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.function.Function;

public interface BounderValue<Costly> {

  Costly costly();

  default InstantiableE<Costly> toQ(Costly costly, Function<Costly, InstantiableE<Costly>> instantiator)
      throws ArithmeticConversionException {
    throw new RuntimeException("has no Q value, or not implemented");
  }
}