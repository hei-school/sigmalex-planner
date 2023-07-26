package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class True extends Constraint {

  public static final NormalizedConstraint TRUE =
      new NormalizedConstraint(new NormalizedLE(0)); // 0 <= 1

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(TRUE));
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
