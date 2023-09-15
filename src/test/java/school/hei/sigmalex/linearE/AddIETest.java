package school.hei.sigmalex.linearE;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.Z;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.LEFactory.add;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;

class AddIETest {
  @Test
  public void add_mono() {
    var x = new Z("x");
    var y = new Q("y");
    assertEquals(
        new NormalizedLE(
            Map.of(
                x, new Constant(3),
                y, new Constant(2.5)),
            ZERO),
        add(mono(3, x), mono(2.5, y)).subnormify());
  }
}