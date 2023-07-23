package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Sigma.SigmaBound;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SigmaZ;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.instantiableE.Constant.ZERO;

class SigmaTest {
  @Test
  public void first_n_sum() {
    var k = new SigmaZ("k");
    int n = 10;
    assertEquals(
        new Normalized(n * (n + 1) / 2.),
        new Sigma(new Mono(k), new SigmaBound(k, 1, n)).normalize());
  }

  @Test
  public void first_arith_progression_sum() {
    // Does Wikipedia know math? https://en.wikipedia.org/wiki/Arithmetic_progression
    int n = 5, a = 2, d = 3;
    var k = new SigmaZ("k");

    assertEquals(
        new Normalized(n / 2. * (2 * a + (n - 1) * d)),
        new Sigma(
            new Add(new Mono(a), new Mult(d, new Add(new Mono(k), new Mono(-1)))),
            new SigmaBound(k, 1, n))
            .normalize());
  }

  @Test
  public void bounded_var() {
    var i = new SigmaZ("i");
    var x_i = new Q("x", List.of(i));
    var le = new Mono(3, x_i);

    var boundI = new SigmaBound(i, 4, 6);
    assertEquals(
        new Normalized(
            Map.of(
                new Q("x_4"), new Constant(3),
                new Q("x_5"), new Constant(3),
                new Q("x_6"), new Constant(3)),
            ZERO),
        new Sigma(le, boundI).normalize());
  }

  @Test
  public void nested_sigma() {
    var i = new SigmaZ("i");
    var j = new SigmaZ("j");
    var le = new Add(new Mono(2, i), new Mono(3, j));

    var boundI = new SigmaBound(i, 4, 6);
    assertEquals(
        new Normalized(Map.of(j, new Constant(9)), new Constant(30)),
        new Sigma(le, boundI).normalize());

    var boundJ = new SigmaBound(j, 10, 11);
    assertEquals(
        new Normalized(Map.of(), new Constant(249)),
        new Sigma(new Sigma(le, boundI), boundJ).normalize());
  }
}
