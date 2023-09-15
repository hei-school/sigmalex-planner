package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Set;

public record Substitute(LinearE le, SubstitutionContext substitutionContext) implements LinearE {

  @Override
  public NormalizedLE normalize() {
    return le.normalize();
  }

  @Override
  public LinearE substitute(SubstitutionContext substitutionContext) {
    return le.substitute(substitutionContext.add(this.substitutionContext));
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}

