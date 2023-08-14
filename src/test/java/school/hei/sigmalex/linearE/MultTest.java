package school.hei.sigmalex.linearE;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.Z;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.vadd;

class MultTest {
  @Test
  public void mult_variadic_add() {
    var x = new Z("x");
    var y = new Q("y");
    assertEquals(
        new NormalizedLE(
            Map.of(
                x, new Constant(6),
                y, new Constant(5)),
            new Constant(2.2)),
        mult(2, vadd(
            mono(0.2),
            mono(2, x), mono(3, y), mono(-0.5, y), mono(1, x),
            mono(0.9))
        ).normify());
  }
}