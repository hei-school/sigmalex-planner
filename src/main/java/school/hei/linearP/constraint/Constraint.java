package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

import static school.hei.linearP.constraint.Le.DEFAULT_EPSILON;

public sealed abstract class Constraint
    permits BiConstraint, BiLeConstraint, False, NormalizedConstraint, Not, True, VariadicAnd, VariadicOr {
  protected final String name;

  public Constraint() {
    this.name = null;
  }

  public Constraint(String name) {
    this.name = name;
  }

  public abstract Set<Set<NormalizedConstraint>> normalize(); // set of set due to Or

  public abstract Set<Variable> variables();

  public String name() {
    return name;
  }

  public static Not not(Constraint constraint) {
    return new Not(constraint);
  }

  public static Le le(String name, LinearE le1, LinearE le2, double epsilon) {
    return new Le(name, le1, le2, epsilon);
  }

  public static Le le(String name, LinearE le1, LinearE le2) {
    return new Le(name, le1, le2, DEFAULT_EPSILON);
  }

  public static Le le(LinearE le1, LinearE le2) {
    return new Le(null, le1, le2, DEFAULT_EPSILON);
  }

  public static And and(String name, Constraint constraint1, Constraint constraint2) {
    return new And(name, constraint1, constraint2);
  }

  public static And and(Constraint constraint1, Constraint constraint2) {
    return new And(null, constraint1, constraint2);
  }
}
