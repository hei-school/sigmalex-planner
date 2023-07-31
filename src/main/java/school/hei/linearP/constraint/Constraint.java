package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.Set;

import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearP.constraint.Le.DEFAULT_EPSILON;

public sealed abstract class Constraint
    permits BiConstraint, BiLeConstraint, False, NormalizedConstraint, Not, PiConstraint, True {

  public static Or or(Constraint constraint1, Constraint constraint2) {
    return new Or(constraint1, constraint2);
  }

  public static Not not(Constraint constraint) {
    return new Not(constraint);
  }

  public static Leq leq(String name, LinearE le1, LinearE le2) {
    return new Leq(le1, le2);
  }

  public static Leq leq(LinearE le1, LinearE le2) {
    return leq(null, le1, le2);
  }

  public static Leq leq(LinearE le, double c) {
    return leq(null, le, mono(c));
  }

  public static Leq leq(Variable v, double c) {
    return leq(null, mono(v), mono(c));
  }

  public static Leq leq(double c, Variable v) {
    return leq(null, mono(c), mono(v));
  }

  public static Le le(String name, LinearE le1, LinearE le2, double epsilon) {
    return new Le(le1, le2, epsilon);
  }

  public static Le le(String name, LinearE le1, LinearE le2) {
    return le(name, le1, le2, DEFAULT_EPSILON);
  }

  public static Le le(LinearE le1, LinearE le2) {
    return le(null, le1, le2, DEFAULT_EPSILON);
  }

  public static Leq geq(LinearE le1, LinearE le2) {
    return new Leq(le2, le1);
  }

  public static Leq geq(Variable v, double c) {
    return geq(mono(v), mono(c));
  }

  public static Leq geq(double c1, double c2) {
    return geq(mono(c1), mono(c2));
  }

  public static Leq geq(LinearE le, double c) {
    return geq(le, mono(c));
  }

  public static And and(Constraint constraint1, Constraint constraint2) {
    return new And(constraint1, constraint2);
  }

  public static Or imply(Constraint constraint1, Constraint constraint2) {
    return or(not(constraint1), constraint2);
  }

  public static And equiv(Constraint constraint1, Constraint constraint2) {
    return and(imply(constraint1, constraint2), imply(constraint2, constraint1));
  }

  public static And eq(LinearE le1, LinearE le2) {
    return and(leq(le1, le2), leq(le2, le1));
  }

  public static And eq(Variable v1, Variable v2) {
    return eq(mono(v1), mono(v2));
  }

  public static And eq(Variable v, LinearE le) {
    return eq(mono(v), le);
  }

  public static And eq(Variable v, double c) {
    return eq(mono(v), mono(c));
  }

  public static Constraint vand(String name, Constraint... constraints) {
    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new And(nested, constraints[i]);
    }
    return nested;
  }

  public static Constraint vand(Constraint... constraints) {
    return vand(null, constraints);
  }

  public static Constraint vor(Constraint... constraints) {
    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new Or(nested, constraints[i]);
    }
    return nested;
  }

  public static PiConstraint pic(Constraint constraint, Bound bound) {
    return new PiConstraint(constraint, bound);
  }

  public static PiConstraint pic(Constraint constraint, Bound... bounds) {
    PiConstraint nested = new PiConstraint(constraint, bounds[0]);
    for (int i = 1; i < bounds.length; i++) {
      nested = new PiConstraint(nested, bounds[i]);
    }
    return nested;
  }

  public abstract DisjunctivePolytopes normalize(); // disjunction is due to Or

  public abstract Set<Variable> variables();
}
