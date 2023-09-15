package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;

public record Mono(InstantiableE e, Optional<Variable> optV) implements LinearE {

  @Override
  public NormalizedLE normalize() {
    return optV
        .map(v -> new NormalizedLE(Map.of(v, e), ZERO))
        .orElseGet(() -> new NormalizedLE(Map.of(), e));
  }

  @Override
  public LinearE substitute(SubstitutionContext substitutionContext) {
    var normalized = normalize();
    var substituted = normalized
        //TODO: need to handle nested instantiation? see weekend_as_bounder_with_nested_dynamic_instantiation
        .substitute(substitutionContext);
    return substituted;
  }

  @Override
  public Set<Variable> variables() {
    return optV.map(Set::of).orElseGet(Set::of);
  }
}
