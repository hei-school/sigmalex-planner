package school.hei.linearE;

import school.hei.linearE.instantiableE.Variable;

import java.util.Arrays;
import java.util.Set;

import static java.util.Comparator.comparing;
import static school.hei.linearE.Sigma.SigmaBound;

public record VariadicSigma(LinearE le, SigmaBound... sigmaBounds) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    var sortedBounds = Arrays.stream(sigmaBounds)
        .sorted(comparing(bound -> bound.bounder().variable().getName()))
        .toList();
    Sigma compoundSigma = new Sigma(le, sortedBounds.get(0));
    for (int i = 1; i < sortedBounds.size(); i++) {
      compoundSigma = new Sigma(compoundSigma, sortedBounds.get(i));
    }
    return compoundSigma.normalize();
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
