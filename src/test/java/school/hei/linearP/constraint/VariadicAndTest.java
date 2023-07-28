package school.hei.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Q;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.vand;

class VariadicAndTest {
  @Test
  public void normalize_two_le() {
    var x = new Q("x");
    var y = new Q("y");

    assertEquals(
        Set.of(Set.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(x, ONE), ZERO)),
            new NormalizedConstraint(new NormalizedLE(Map.of(y, ONE), ZERO)))),
        vand(leq(x, 0), leq(y, 0)).normalize());
  }
}