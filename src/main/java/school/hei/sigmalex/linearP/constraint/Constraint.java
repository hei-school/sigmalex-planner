package school.hei.sigmalex.linearP.constraint;

import lombok.extern.slf4j.Slf4j;
import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.instantiableE.Bound.toBSubstitutionContexts;
import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.Le.DEFAULT_EPSILON;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

@Slf4j
public sealed abstract class Constraint
    permits BiLeConstraint, False, ListConstraint, NormalizedConstraint, Not, PiConstraint, True {

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

  public static Constraint eq(LinearE le, double c) {
    return eq(le, mono(c));
  }

  //TODO: contextual instantiation from sigma inside pic does __not__ work with neq
  public static Constraint neq(LinearE le, double c) {
    return not(eq(le, mono(c)));
  }

  public static Constraint eq(Variable v, double c) {
    return eq(mono(v), mono(c));
  }

  public static Constraint and(Constraint... constraints) {
    return new And(Arrays.stream(constraints).toList());
  }

  public static Constraint and(Set<Constraint> constraints) {
    return new And(constraints.stream().toList());
  }

  public static Constraint or(Constraint... constraints) {
    if (constraints.length == 0) {
      return FALSE;
    }

    return new Or(Arrays.stream(constraints).toList());
  }

  public static Constraint pic(Constraint constraint, Bound... bounds) {
    var optSubCtx = toBSubstitutionContexts(bounds);
    return optSubCtx
        .map(subCtx -> (Constraint) new PiConstraint(constraint, subCtx))
        .orElse(TRUE);
  }

  public abstract DisjunctivePolytopes/*disjunction is due to Or*/ normalize(SubstitutionContext substitutionContext);

  public DisjunctivePolytopes normalize() {
    return normalize(new SubstitutionContext(new HashMap<>()));
  }

  public DisjunctivePolytopes normify() {
    return normalize().simplify();
  }

  public abstract Set<Variable> variables();
}
