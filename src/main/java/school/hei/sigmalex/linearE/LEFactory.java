package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Arrays;
import java.util.List;
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
    return new Add(List.of(e1, e2));
  }

  public static Add add(Variable v1, Variable v2) {
    return add(mono(v1), mono(v2));
  }

  public static Add add(Variable v1, LinearE le2) {
    return add(mono(v1), le2);
  }

  public static Add add(LinearE le1, Variable v2) {
    return add(le1, mono(v2));
  }

  public static Add add(LinearE le1, double c) {
    return add(le1, mono(c));
  }

  public static Mult mult(double c, LinearE le) {
    return new Mult(new Constant(c), le);
  }

  public static Mult mult(InstantiableE e, Variable v) {
    return new Mult(e, mono(v));
  }

  public static Mult mult(InstantiableE e, LinearE le) {
    return new Mult(e, le);
  }

  public static Mult mult(double c, Variable v) {
    return new Mult(new Constant(c), mono(v));
  }

  public static Add sub(LinearE le1, LinearE le2) {
    return add(le1, new Mult(new Constant(-1), le2));
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

  public static LinearE vadd(LinearE... leArray) {
    return new Add(Arrays.stream(leArray).toList());
  }

  public static LinearE vadd(Variable... variables) {
    return vadd(Arrays.stream(variables)
        .map(LEFactory::mono)
        .toArray(Mono[]::new));
  }

  public static LinearE sigma(LinearE le, Bound... bounds) {
    var substitutionContextsOpt = Bound.toBSubstitutionContexts(bounds);
    if (substitutionContextsOpt.isEmpty()) {
      return mono(0);
    }
    var substitutionContexts = substitutionContextsOpt.get();

    LinearE res = mono(Constant.ZERO);
    for (var substitutionContext : substitutionContexts) {
      res = add(res, le.substitute(substitutionContext));
    }

    return res;
  }

  public static LinearE sigma(Variable le, Bound... bounds) {
    return sigma(mono(le), bounds);
  }
}
