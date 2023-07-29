package school.hei.linearP.constraint.polytope;


import school.hei.linearP.constraint.NormalizedConstraint;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Polytope(Set<NormalizedConstraint> constraints) {

  public static Polytope of(NormalizedConstraint... constraints) {
    return new Polytope(Arrays.stream(constraints).collect(toSet()));
  }
}