package school.hei.sigmalex.linearP.constraint.polytope;

import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record ConjunctivePolytopes(Set<Polytope> polytopes) {

  public static ConjunctivePolytopes of(Polytope... polytopes) {
    return new ConjunctivePolytopes(Arrays.stream(polytopes).collect(toSet()));
  }

  public ConjunctivePolytopes add(ConjunctivePolytopes toAdd) {
    var newPolytopes = new HashSet<>(polytopes);
    newPolytopes.addAll(toAdd.polytopes);
    return new ConjunctivePolytopes(newPolytopes);
  }

  public Polytope merge() {
    var constraints = new ArrayList<NormalizedConstraint>();
    for (var polytope : polytopes) {
      constraints.addAll(polytope.constraints());
    }
    return Polytope.of(constraints.toArray(NormalizedConstraint[]::new));
  }
}