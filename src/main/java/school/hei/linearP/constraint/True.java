package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class True extends Constraint {
  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(new NormalizedConstraint(new NormalizedLE(0)))); // 0 <= 1
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
