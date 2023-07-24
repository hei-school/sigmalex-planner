package school.hei.linearE.instantiableE;

public interface BounderValue {
  default InstantiableE toArithmeticValue() throws ArithmeticConversion {
    throw new ArithmeticConversion(this.toString());
  }
}
