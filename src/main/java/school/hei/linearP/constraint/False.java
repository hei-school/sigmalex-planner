package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class False extends Constraint {

  public static final NormalizedConstraint FALSE =
      new NormalizedConstraint(new NormalizedLE(1)); // 1 <= 0

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(FALSE));
  }

  @Override
  public Set<Variable> variables() {
    return Set.of();
  }
}
