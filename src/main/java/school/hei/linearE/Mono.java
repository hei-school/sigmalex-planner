package school.hei.linearE;

import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static school.hei.linearE.instantiableE.Constant.ZERO;

public record Mono(InstantiableE e, Optional<Variable> optV) implements LinearE {

  @Override
  public NormalizedLE normalize() {
    return optV
        .map(v -> new NormalizedLE(Map.of(v, e), ZERO))
        .orElseGet(() -> new NormalizedLE(Map.of(), e));
  }

  @Override
  public Set<Variable> variables() {
    return optV.map(Set::of).orElseGet(Set::of);
  }
}
