package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Set;

public record Sigma(LinearE le, SubstitutionContext substitutionContext) implements LinearE {

  @Override
  public NormalizedLE normalize(SubstitutionContext substitutionContext) {
    return le.normalize(substitutionContext.add(this.substitutionContext)).substituteAll(substitutionContext);
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
