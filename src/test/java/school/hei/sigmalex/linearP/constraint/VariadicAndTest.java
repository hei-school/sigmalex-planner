package school.hei.sigmalex.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ONE;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;

class VariadicAndTest {
  @Test
  public void normalize_two_le() {
    var x = new Q("x");
    var y = new Q("y");

    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(x, ONE), ZERO)),
            new NormalizedConstraint(new NormalizedLE(Map.of(y, ONE), ZERO)))),
        and(leq(x, 0), leq(y, 0)).normalize().simplify());
  }
}