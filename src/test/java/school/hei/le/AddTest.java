package school.hei.le;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddTest {
  @Test
  public void add_mono() {
    var x = new ZVariable("x");
    var y = new QVariable("y");
    assertEquals(
        new Normalized(
            Map.of(
                x, 3.,
                y, 2.5),
            0),
        new Add(new Mono(3, x), new Mono(2.5, y)).normalize());
  }
}