package school.hei.linearE;

import school.hei.linearE.instantiableE.Variable;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public record Sub(LinearE le1, LinearE le2) implements LinearE {

  public Sub(double c, LinearE le) {
    this(new Mono(c), le);
  }

  public Sub(LinearE le, double c) {
    this(le, new Mono(c));
  }

  public Sub(LinearE le, Variable v) {
    this(le, new Mono(v));
  }

  @Override
  public NormalizedLE normalize() {
    return new Add(le1, new Mult(-1, le2)).normalize();
  }

  @Override
  public Set<Variable> variables() {
    return Stream.concat(
        le1.variables().stream(),
        le2.variables().stream()
    ).collect(toSet());
  }
}
