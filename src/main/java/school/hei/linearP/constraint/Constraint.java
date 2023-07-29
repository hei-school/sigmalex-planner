package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.Mono;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.Set;

import static school.hei.linearP.constraint.Le.DEFAULT_EPSILON;

public sealed abstract class Constraint
    permits BiConstraint, BiLeConstraint, False, NormalizedConstraint, Not, PiConstraint, True {
  protected final String name;

  public Constraint() {
    this.name = null;
  }

  public Constraint(String name) {
    this.name = name;
  }

  public abstract DisjunctivePolytopes normalize(); // disjunction is due to Or

  public abstract Set<Variable> variables();

  public String name() {
    return name;
  }


  public static Or or(String name, Constraint constraint1, Constraint constraint2) {
    return new Or(name, constraint1, constraint2);
  }

  public static Or or(Constraint constraint1, Constraint constraint2) {
    return or(null, constraint1, constraint2);
  }

  public static Not not(Constraint constraint) {
    return new Not(constraint);
  }

  public static Leq leq(String name, LinearE le1, LinearE le2) {
    return new Leq(name, le1, le2);
  }

  public static Leq leq(LinearE le1, LinearE le2) {
    return leq(null, le1, le2);
  }

  public static Leq leq(LinearE le, double c) {
    return leq(null, le, new Mono(c));
  }

  public static Leq leq(Variable v, double c) {
    return leq(null, new Mono(v), new Mono(c));
  }

  public static Le le(String name, LinearE le1, LinearE le2, double epsilon) {
    return new Le(name, le1, le2, epsilon);
  }

  public static Le le(String name, LinearE le1, LinearE le2) {
    return le(name, le1, le2, DEFAULT_EPSILON);
  }

  public static Le le(LinearE le1, LinearE le2) {
    return le(null, le1, le2, DEFAULT_EPSILON);
  }

  public static Leq geq(String name, LinearE le1, LinearE le2) {
    return new Leq(name, le2, le1);
  }

  public static Leq geq(Variable v, double c) {
    return geq(null, new Mono(v), new Mono(c));
  }

  public static Leq geq(double c1, double c2) {
    return geq(null, new Mono(c1), new Mono(c2));
  }

  public static Leq geq(LinearE le, double c) {
    return geq(null, le, new Mono(c));
  }

  public static And and(String name, Constraint constraint1, Constraint constraint2) {
    return new And(name, constraint1, constraint2);
  }

  public static And and(Constraint constraint1, Constraint constraint2) {
    return and(null, constraint1, constraint2);
  }

  public static And eq(String name, LinearE le1, LinearE le2) {
    return and(name, leq(le1, le2), leq(le2, le1));
  }

  public static And eq(Variable v1, Variable v2) {
    return eq(null, new Mono(v1), new Mono(v2));
  }

  public static Constraint vand(String name, Constraint... constraints) {
    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new And(name, nested, constraints[i]);
    }
    return nested;
  }

  public static Constraint vand(Constraint... constraints) {
    return vand(null, constraints);
  }

  public static Constraint vor(String name, Constraint... constraints) {
    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new Or(name, nested, constraints[i]);
    }
    return nested;
  }

  public static Constraint vor(Constraint... constraints) {
    return vor(null, constraints);
  }

  public static PiConstraint pic(String name, Constraint constraint, Bound bound) {
    return new PiConstraint(name, constraint, bound);
  }

  public static PiConstraint pic(Constraint constraint, Bound bound) {
    return new PiConstraint(null, constraint, bound);
  }
}
