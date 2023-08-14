package school.hei.sigmalex.linearP.constraint;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.Bounder;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.HashMap;
import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearP.constraint.Le.DEFAULT_EPSILON;

public sealed abstract class Constraint
    permits BiConstraint, BiLeConstraint, False, NormalizedConstraint, Not, PiConstraint, True {

  public static Not not(Constraint constraint) {
    return new Not(constraint);
  }

  public static Leq leq(LinearE le1, LinearE le2) {
    return new Leq(le1, le2);
  }

  public static Leq leq(LinearE le, double c) {
    return leq(le, mono(c));
  }

  public static Leq leq(Variable v, double c) {
    return leq(mono(v), mono(c));
  }

  public static Leq leq(Variable v, LinearE le) {
    return leq(mono(v), le);
  }

  public static Leq leq(double c, Variable v) {
    return leq(mono(c), mono(v));
  }

  public static Le le(LinearE le1, LinearE le2, double epsilon) {
    return new Le(le1, le2, epsilon);
  }

  public static Le le(LinearE le1, LinearE le2) {
    return new Le(le1, le2, DEFAULT_EPSILON);
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

  public static Constraint imply(Constraint constraint1, Constraint constraint2) {
    return or(not(constraint1), constraint2);
  }

  public static Constraint equiv(Constraint constraint1, Constraint constraint2) {
    return and(imply(constraint1, constraint2), imply(constraint2, constraint1));
  }

  public static Constraint eq(LinearE le1, LinearE le2) {
    return and(leq(le1, le2), leq(le2, le1));
  }

  public static Constraint eq(Variable v1, Variable v2) {
    return eq(mono(v1), mono(v2));
  }

  public static Constraint eq(Variable v, LinearE le) {
    return eq(mono(v), le);
  }

  //TODO: contextual instantiation from sigma inside pic does __not__ work with neq
  public static Constraint neq(LinearE le, double c) {
    return not(eq(le, mono(c)));
  }

  public static Constraint eq(Variable v, double c) {
    return eq(mono(v), mono(c));
  }

  public static Constraint and(Constraint... constraints) {
    if (constraints.length == 0) {
      return True.TRUE;
    }

    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new And(nested, constraints[i]);
    }
    return nested;
  }

  public static Constraint or(Constraint... constraints) {
    Constraint nested = constraints[0];
    for (int i = 1; i < constraints.length; i++) {
      nested = new Or(nested, constraints[i]);
    }
    return nested;
  }

  public static Constraint pic(Constraint constraint, Bound... bounds) {
    var optSubCtx = Bound.toBSubstitutionContexts(bounds);
    return optSubCtx
        .map(subCtx -> pic(constraint, subCtx))
        .orElse(True.TRUE);
  }

  private static Constraint pic(Constraint constraint, Set<SubstitutionContext> substitutionContexts) {
    Constraint res = True.TRUE;
    for (var substitutionContext : substitutionContexts) {
      res = new And(res, normalizedSubstitution(constraint, substitutionContext));
    }
    return res;
  }

  private static Constraint normalizedSubstitution(
      Constraint constraint, SubstitutionContext substitutionContext) {
    var normalized = constraint.normalize(substitutionContext);
    for (Bounder bounder : substitutionContext.substitutions().keySet()) {
      normalized = normalized.substitute(bounder, substitutionContext.get(bounder), substitutionContext);
    }
    return normalized.toDnf();
  }

  public abstract DisjunctivePolytopes normalize(SubstitutionContext substitutionContext); // disjunction is due to Or

  public DisjunctivePolytopes normalize() {
    return normalize(new SubstitutionContext(new HashMap<>()));
  }

  public abstract Set<Variable> variables();
}
