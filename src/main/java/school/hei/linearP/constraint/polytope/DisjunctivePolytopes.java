package school.hei.linearP.constraint.polytope;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record DisjunctivePolytopes(Set<Polytope> polytopes) {

  public static DisjunctivePolytopes of(Polytope... polytopes) {
    return new DisjunctivePolytopes(Arrays.stream(polytopes).collect(toSet()));
  }

  public void add(Polytope polytope) {
    polytopes.add(polytope);
  }
}