package school.hei.sigmalex.linearE;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.Z;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.LEFactory.vadd;

class VariadicAddTestE {

  @Test
  public void add_constants() {
    assertEquals(
        new NormalizedLE(4),
        vadd(mono(1), mono(3)).subnormify());
    assertEquals(
        new NormalizedLE(2.5),
        vadd(mono(0.2), mono(1), mono(1.3)).subnormify());
  }

  @Test
  public void add_mono() {
    var x = new Z("x");
    var y = new Q("y");
    assertEquals(
        new NormalizedLE(
            Map.of(
                x, new Constant(3.),
                y, new Constant(2.5)),
            new Constant(1.1)),
        vadd(
            mono(0.2),
            mono(2, x), mono(3, y), mono(-0.5, y), mono(1, x),
            mono(0.9))
            .subnormify());
  }
}