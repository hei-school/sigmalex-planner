package school.hei.le;

import org.junit.jupiter.api.Test;
import school.hei.le.Sigma.Bound;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SigmaTest {
  @Test
  public void first_n_sum() {
    var k = new Z("k");
    int n = 10;
    assertEquals(
        new Normalized(Map.of(), n * (n + 1) / 2.),
        new Sigma(new Mono(k), new Bound(k, 1, n)).normalize());
  }

  @Test
  public void first_arith_progression_sum() {
    // Does Wikipedia know math? https://en.wikipedia.org/wiki/Arithmetic_progression
    int n = 5, a = 2, d = 3;
    var k = new Z("k");
    assertEquals(
        new Normalized(Map.of(), n / 2. * (2 * a + (n - 1) * d)),
        new Sigma(
            new Add(new Mono(a), new Mult(d, new Add(new Mono(k), new Mono(-1)))),
            new Bound(k, 1, n))
            .normalize());
  }

  @Test
  public void nested_sigma() {
    var i = new Z("i");
    var j = new Z("j");
    var le = new Add(new Mono(2, i), new Mono(3, j));

    var boundI = new Bound(i, 4, 6);
    assertEquals(
        new Normalized(Map.of(j, 9.), 30),
        new Sigma(le, boundI).normalize());

    var boundJ = new Bound(j, 10, 11);
    assertEquals(
        new Normalized(Map.of(), 249),
        new Sigma(new Sigma(le, boundI), boundJ).normalize());
  }
}
