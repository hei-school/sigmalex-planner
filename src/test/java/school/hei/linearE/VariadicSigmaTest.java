package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Sigma.SigmaBound;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SigmaZ;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariadicSigmaTest {
  @Test
  public void nested_sigma_as_variadic() {
    var i = new SigmaZ("i");
    var j = new SigmaZ("j");
    var le = new Add(new Mono(2, i), new Mono(3, j));

    var boundI = new SigmaBound(i, 4, 6);
    var boundJ = new SigmaBound(j, 10, 11);
    assertEquals(
        new VariadicSigma(le, boundI, boundJ).normalize(),
        new Sigma(new Sigma(le, boundI), boundJ).normalize());
  }

  @Test
  public void bounded_vars() {
    var i = new SigmaZ("i");
    var j = new SigmaZ("j");
    var x_i_j = new Q("x", List.of(i, j));
    var le_i_j = new VariadicAdd(new Mono(i), new Mono(j), new Mono(3, x_i_j));

    var boundI = new SigmaBound(i, 4, 6);
    var boundJ = new SigmaBound(j, 10, 11);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Q("x_4_10"), new Constant(3),
                new Q("x_5_10"), new Constant(3),
                new Q("x_6_10"), new Constant(3),
                new Q("x_4_11"), new Constant(3),
                new Q("x_5_11"), new Constant(3),
                new Q("x_6_11"), new Constant(3)),
            new Constant(93)),
        new VariadicSigma(le_i_j, boundI, boundJ).normalize());
  }
}