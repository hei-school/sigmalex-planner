package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Sigma.SigmaBound;
import school.hei.linearE.instantiableE.SigmaZ;

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
}