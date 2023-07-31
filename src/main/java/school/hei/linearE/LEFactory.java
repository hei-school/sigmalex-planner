package school.hei.linearE;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Arrays;
import java.util.Optional;

public class LEFactory {

  public static Mono mono(double c) {
    return new Mono(new Constant(c), Optional.empty());
  }

  public static Mono mono(Constant c) {
    return new Mono(c, Optional.empty());
  }

  public static Mono mono(double c, Variable v) {
    return new Mono(new Constant(c), Optional.of(v));
  }

  public static Mono mono(Variable v) {
    return mono(1, v);
  }


  public static Add add(LinearE e1, LinearE e2) {
    return new Add(e1, e2);
  }

  public static Add add(Variable v1, Variable v2) {
    return new Add(mono(v1), mono(v2));
  }

  public static Add add(Variable v1, LinearE le2) {
    return new Add(mono(v1), le2);
  }

  public static Add add(LinearE le1, Variable v2) {
    return new Add(le1, mono(v2));
  }

  public static Add add(LinearE le1, double c) {
    return new Add(le1, mono(c));
  }

  public static Mult mult(double c, LinearE le) {
    return new Mult(new Constant(c), le);
  }

  public static Mult mult(InstantiableE e, Variable v) {
    return new Mult(e, mono(v));
  }

  public static Add sub(LinearE le1, LinearE le2) {
    return new Add(le1, new Mult(new Constant(-1), le2));
  }

  public static Add sub(double c, LinearE le) {
    return sub(mono(c), le);
  }

  public static Add sub(LinearE le, double c) {
    return sub(le, mono(c));
  }

  public static Add sub(LinearE le, Variable v) {
    return sub(le, mono(v));
  }

  public static LinearE vadd(LinearE... leList) {
    return Arrays.stream(leList)
        .reduce(Add::new)
        .orElse(mono(0.));
  }

  public static LinearE vadd(Variable... vList) {
    return vadd(Arrays.stream(vList)
        .map(LEFactory::mono)
        .toArray(Mono[]::new));
  }

  public static LinearE vsigma(LinearE le, Bound... bounds) {
    Sigma compoundSigma = new Sigma(le, bounds[0]);
    for (int i = 1; i < bounds.length; i++) {
      compoundSigma = new Sigma(compoundSigma, bounds[i]);
    }
    return compoundSigma;
  }

  public static LinearE vsigma(Variable le, Bound... bounds) {
    return vsigma(mono(le), bounds);
  }
}
