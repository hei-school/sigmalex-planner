package school.hei.linearP.constraint.polytope;

import school.hei.linearE.instantiableE.Bounder;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearP.constraint.Constraint;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.Constraint.or;

public record DisjunctivePolytopes(Set<Polytope> polytopes) {

  public static DisjunctivePolytopes of(Polytope... polytopes) {
    return new DisjunctivePolytopes(Arrays.stream(polytopes).collect(toSet()));
  }

  public void add(Polytope polytope) {
    polytopes.add(polytope);
  }

  public DisjunctivePolytopes substitute(Bounder bounder, BounderValue bounderValue, SubstitutionContext substitutionContext) {
    DisjunctivePolytopes res = new DisjunctivePolytopes(polytopes.stream()
        .map(polytope -> polytope.substitute(bounder, bounderValue, substitutionContext))
        .collect(toSet()));
    return res;
  }

  public Constraint toDnf() {
    return or(polytopes.stream()
        .map(Polytope::toCnf)
        .toArray(Constraint[]::new));
  }

  public DisjunctivePolytopes simplify() {
    return new DisjunctivePolytopes(polytopes.stream()
        .map(Polytope::simplify)
        .collect(toSet()));
  }
}