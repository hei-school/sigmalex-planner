package school.hei.linearP.constraint;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.Arrays;
import java.util.Set;

import static school.hei.linearP.constraint.True.TRUE;

public final class PiConstraint extends Constraint {

  private final Constraint constraint;
  private final Bound bound;

  public PiConstraint(String name, Constraint constraint, Bound bound) {
    super(name);
    this.constraint = constraint;
    this.bound = bound;
  }

  @Override
  public DisjunctivePolytopes normalize() {
    return Arrays.stream(bound.values())
        .map(bounderValue -> constraint.normalize()
            .substitute(bound.bounder(), bounderValue)
            .toDnf())
        .reduce(TRUE, Constraint::vand)
        .normalize();
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
