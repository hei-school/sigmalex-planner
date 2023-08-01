package school.hei.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.and;

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