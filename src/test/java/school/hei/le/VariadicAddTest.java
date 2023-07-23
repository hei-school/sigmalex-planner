package school.hei.le;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariadicAddTest {

  @Test
  public void add_constants() {
    assertEquals(
        new Normalized(Map.of(), 4),
        new VariadicAdd(new Mono(1), new Mono(3)).normalize());
    assertEquals(
        new Normalized(Map.of(), 2.6),
        new VariadicAdd(new Mono(0.3), new Mono(1), new Mono(1.3)).normalize());
  }

  @Test
  public void add_mono() {
    var x = new ZVariable("x");
    var y = new QVariable("y");
    assertEquals(
        new Normalized(
            Map.of(
                x, 3.,
                y, 2.5),
            1.1),
        new VariadicAdd(
            new Mono(0.2),
            new Mono(2, x), new Mono(3, y), new Mono(-0.5, y), new Mono(1, x),
            new Mono(0.9))
            .normalize());
  }
}