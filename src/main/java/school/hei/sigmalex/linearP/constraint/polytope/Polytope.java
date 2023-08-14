package school.hei.sigmalex.linearP.constraint.polytope;


import school.hei.sigmalex.linearE.instantiableE.Bounder;
import school.hei.sigmalex.linearE.instantiableE.BounderValue;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.Constraint;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Polytope(Set<NormalizedConstraint> constraints) {

  public static Polytope of(NormalizedConstraint... constraints) {
    return new Polytope(Arrays.stream(constraints).collect(toSet()));
  }

  public Constraint toCnf() {
    return Constraint.and(constraints.toArray(Constraint[]::new));
  }

  public Polytope substitute(Bounder bounder, BounderValue bounderValue, SubstitutionContext substitutionContext) {
    return new Polytope(constraints.stream()
        .map(constraint -> constraint.substitute(bounder, bounderValue, substitutionContext))
        .collect(toSet()));
  }

  public Polytope simplify() {
    return new Polytope(constraints.stream()
        .map(NormalizedConstraint::simplify)
        .collect(toSet()));
  }
}