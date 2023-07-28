package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.NormalizedLE.DuplicateVariableName;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.linearE.instantiableE.Constant.ZERO;

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
        new Add(new Mono(3, x), new Mono(2.5, y)).normalize());
  }

  @Test
  public void duplicate_names_prohibited() {
    var x = new Z("x");
    var y = new Q("x");

    var e = assertThrows(
        DuplicateVariableName.class,
        () -> new Add(new Mono(3, x), new Mono(2.5, y)).normalize());
  }
}