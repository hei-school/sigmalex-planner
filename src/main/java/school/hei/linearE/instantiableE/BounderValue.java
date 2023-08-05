package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

public interface BounderValue<Costly> {

  Costly costly();

  default InstantiableE<Costly> toQ(
      Costly costly, SubstitutionContext<Costly> substitutionContext, Instantiator<Costly> instantiator)
      throws ArithmeticConversionException {
    return instantiator.apply(costly, substitutionContext);
  }
}