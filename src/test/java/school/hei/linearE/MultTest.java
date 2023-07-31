package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.vadd;

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
            new Mono(0.2),
            new Mono(2, x), new Mono(3, y), new Mono(-0.5, y), new Mono(1, x),
            new Mono(0.9))
        ).normalize());
  }
}