package school.hei.linearE;

import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;

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
