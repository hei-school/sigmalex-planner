package school.hei.linearE;

import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Map;
import java.util.Optional;

import static school.hei.linearE.instantiableE.Constant.ZERO;

public record Mono(InstantiableE e, Optional<Variable> optV) implements LinearE {

  public Mono(double c) {
    this(new Constant(c), Optional.empty());
  }

  public Mono(double c, Variable v) {
    this(new Constant(c), Optional.of(v));

  }

  public Mono(Variable v) {
    this(1, v);
  }

  @Override
  public Normalized normalize() {
    return optV
        .map(v -> new Normalized(Map.of(v, e), ZERO))
        .orElseGet(() -> new Normalized(Map.of(), e));
  }
}
