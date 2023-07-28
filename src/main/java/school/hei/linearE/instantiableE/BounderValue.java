package school.hei.linearE.instantiableE;

public interface BounderValue {
  default InstantiableE toArithmeticValue() throws ArithmeticConversionException {
    throw new ArithmeticConversionException(this.toString());
  }
}
