package school.hei.le;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultTest {
  @Test
  public void mult_variadic_add() {
    var x = new Z("x");
    var y = new Q("y");
    assertEquals(
        new Normalized(
            Map.of(
                x, 6.,
                y, 5.),
            2.2),
        new Mult(2, new VariadicAdd(
            new Mono(0.2),
            new Mono(2, x), new Mono(3, y), new Mono(-0.5, y), new Mono(1, x),
            new Mono(0.9))
        ).normalize());
  }
}