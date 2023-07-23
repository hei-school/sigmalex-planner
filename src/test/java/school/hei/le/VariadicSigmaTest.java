package school.hei.le;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariadicSigmaTest {
  @Test
  public void nested_sigma_as_variadic() {
    var i = new ZVariable("i");
    var j = new ZVariable("j");
    var le = new Add(new Mono(2, i), new Mono(3, j));

    var boundI = new Sigma.Bound(i, 4, 6);
    var boundJ = new Sigma.Bound(j, 10, 11);
    assertEquals(
        new VariadicSigma(le, boundI, boundJ).normalize(),
        new Sigma(new Sigma(le, boundI), boundJ).normalize());
  }
}