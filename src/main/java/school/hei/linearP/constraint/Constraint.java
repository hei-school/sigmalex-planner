package school.hei.linearP.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BoundedValue;
import school.hei.linearE.instantiableE.Bounder;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.set.CartesianProduct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearP.constraint.Le.DEFAULT_EPSILON;
import static school.hei.linearP.constraint.True.TRUE;

public sealed abstract class Constraint
    permits BiConstraint, BiLeConstraint, False, NormalizedConstraint, Not, PiConstraint, True {

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

  public static Constraint eq(Variable v, double c) {
    return eq(mono(v), mono(c));
  }

  public static Constraint and(Constraint... constraints) {
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

  private static Constraint pic(Constraint constraint, Set<SubstitutionContext> substitutionContexts) {
    Constraint res = TRUE;
    for (var substitutionContext : substitutionContexts) {
      res = new And(res, normalizedSubstitution(constraint, substitutionContext));
    }
    return res;
  }

  private static Constraint normalizedSubstitution(
      Constraint constraint, SubstitutionContext<?> substitutionContext) {
    var normalized = constraint.normalize(substitutionContext);
    for (Bounder bounder : substitutionContext.substitutions().keySet()) {
      normalized = normalized.substitute(bounder, substitutionContext.get(bounder), substitutionContext);
    }
    return normalized.toDnf();
  }


  public static Constraint pic(Constraint constraint, Bound... bounds) {
    var bounderAndValuesArray = Arrays.stream(bounds)
        .map(bound -> Arrays.stream(bound.values())
            .map(bounderValue -> new BoundedValue(bound.bounder(), bounderValue))
            .collect(toSet()))
        .toArray(Set[]::new);
    var cp = CartesianProduct.cartesianProduct(bounderAndValuesArray);

    var substitutionContexts = new HashSet<SubstitutionContext>();
    for (var bounderAndValues : cp) {
      if (bounderAndValues instanceof BoundedValue) {
        substitutionContexts.add(new SubstitutionContext(
            Map.of(((BoundedValue) bounderAndValues).bounder(), ((BoundedValue) bounderAndValues).bounderValue())));
        continue;
      }
      var substitutionContextMap = new HashMap<>();
      Set<BoundedValue> bounderAndValuesAsSet = (Set) bounderAndValues;
      bounderAndValuesAsSet.forEach(boundedValue ->
          substitutionContextMap.put(boundedValue.bounder(), boundedValue.bounderValue()));
      substitutionContexts.add(new SubstitutionContext(substitutionContextMap));
    }

    return pic(constraint, substitutionContexts);
  }

  public abstract DisjunctivePolytopes normalize(SubstitutionContext substitutionContext); // disjunction is due to Or

  public DisjunctivePolytopes normalize() {
    return normalize(new SubstitutionContext(new HashMap<>()));
  }

  public abstract Set<Variable> variables();
}
