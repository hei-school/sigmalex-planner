package school.hei.linearE;

import school.hei.linearE.instantiableE.Variable;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record VariadicAdd(LinearE... leList) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    return Arrays.stream(leList)
        .reduce(Add::new)
        .orElse(new Mono(0.))
        .normalize();
  }

  @Override
  public Set<Variable> variables() {
    return Arrays.stream(leList)
        .flatMap(le -> le.variables().stream())
        .collect(toSet());
  }
}
