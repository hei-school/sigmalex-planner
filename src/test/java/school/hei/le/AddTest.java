package school.hei.le;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddTest {
  @Test
  public void add_mono() {
    var x = new Z("x");
    var y = new Q("y");
    assertEquals(
        new Normalized(
            Map.of(
                x, 3.,
                y, 2.5),
            0),
        new Add(new Mono(3, x), new Mono(2.5, y)).normalize());
  }
}