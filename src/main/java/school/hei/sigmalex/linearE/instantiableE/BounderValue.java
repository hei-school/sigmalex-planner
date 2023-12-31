package school.hei.sigmalex.linearE.instantiableE;

import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;

public interface BounderValue<Costly> {

  Costly costly();

  default InstantiableE toQ(
      Costly costly, SubstitutionContext substitutionContext, Instantiator<Costly> instantiator)
      throws ArithmeticConversionException {
    return instantiator.apply(costly, substitutionContext);
  }
}