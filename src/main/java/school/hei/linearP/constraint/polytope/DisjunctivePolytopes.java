package school.hei.linearP.constraint.polytope;

import school.hei.linearE.instantiableE.Bounder;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearP.constraint.Constraint;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.Constraint.vor;

public record DisjunctivePolytopes(Set<Polytope> polytopes) {

  public static DisjunctivePolytopes of(Polytope... polytopes) {
    return new DisjunctivePolytopes(Arrays.stream(polytopes).collect(toSet()));
  }

  public void add(Polytope polytope) {
    polytopes.add(polytope);
  }

  public DisjunctivePolytopes substitute(Bounder bounder, BounderValue bounderValue) {
    DisjunctivePolytopes res = new DisjunctivePolytopes(polytopes.stream()
        .map(polytope -> polytope.substitute(bounder, bounderValue))
        .collect(toSet()));
    return res;
  }

  public Constraint toDnf() {
    return vor(polytopes.stream()
        .map(Polytope::toCnf)
        .toArray(Constraint[]::new));
  }
}