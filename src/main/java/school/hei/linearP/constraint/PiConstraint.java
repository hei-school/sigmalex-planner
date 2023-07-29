package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public final class PiConstraint extends Constraint {

  private final Constraint constraint;
  private final Bound bound;

  public PiConstraint(String name, Constraint constraint, Bound bound) {
    super(name);
    this.constraint = constraint;
    this.bound = bound;
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return constraint.normalize(); //TODO
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
