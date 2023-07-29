package school.hei.linearE;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Variable;

import java.util.Arrays;

import static java.util.Comparator.comparing;

public class LEFactory {

  public static Add sub(LinearE le1, LinearE le2) {
    return new Add(le1, new Mult(-1, le2));
  }

  public static Add sub(double c, LinearE le) {
    return sub(new Mono(c), le);
  }

  public static Add sub(LinearE le, double c) {
    return sub(le, new Mono(c));
  }

  public static Add sub(LinearE le, Variable v) {
    return sub(le, new Mono(v));
  }

  public static LinearE vadd(LinearE... leList) {
    return Arrays.stream(leList)
        .reduce(Add::new)
        .orElse(new Mono(0.));
  }

  public static LinearE vsigma(LinearE le, Bound... bounds) {
    var sortedBounds = Arrays.stream(bounds)
        .sorted(comparing(bound -> bound.bounder().variable().getName()))
        .toList();
    Sigma compoundSigma = new Sigma(le, sortedBounds.get(0));
    for (int i = 1; i < sortedBounds.size(); i++) {
      compoundSigma = new Sigma(compoundSigma, sortedBounds.get(i));
    }
    return compoundSigma;
  }
}
