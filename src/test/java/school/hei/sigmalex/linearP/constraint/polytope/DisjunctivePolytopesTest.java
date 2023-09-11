package school.hei.sigmalex.linearP.constraint.polytope;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Z;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisjunctivePolytopesTest {

  @Test
  void identity() {
    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), new Constant<>(1),
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), new Constant<>(0)),
                new Constant<>(7)))
        )),
        DisjunctivePolytopes.of(Polytope.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), new Constant<>(1),
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), new Constant<>(0)),
                new Constant<>(7))))));
  }
}