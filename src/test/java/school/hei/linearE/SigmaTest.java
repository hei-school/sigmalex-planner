package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.SigmaTest.Days.saturday;
import static school.hei.linearE.SigmaTest.Days.sunday;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;

class SigmaTest {
  @Test
  public void first_n_sum() {
    var k = new BounderZ("k");
    int n = 10;
    assertEquals(
        new NormalizedLE(n * (n + 1) / 2.),
        new Sigma(new Mono(k), new Bound(k, 1, n)).normalize().simplify());
  }

  @Test
  public void first_arith_progression_sum() {
    // Does Wikipedia know math? https://en.wikipedia.org/wiki/Arithmetic_progression
    int n = 5, a = 2, d = 3;
    var k = new BounderZ("k");

    assertEquals(
        new NormalizedLE(n / 2. * (2 * a + (n - 1) * d)),
        new Sigma(
            new Add(new Mono(a), mult(d, new Add(new Mono(k), new Mono(-1)))),
            new Bound(k, 1, n))
            .normalize().simplify());
  }

  @Test
  public void bounded_vars() {
    var i = new BounderZ("i");
    var x_i = new Q("x", Set.of(i));
    var le_i = new Mono(3, x_i);

    var boundI = new Bound(i, 4, 6);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Q("x[i:4]"), new Constant(3),
                new Q("x[i:5]"), new Constant(3),
                new Q("x[i:6]"), new Constant(3)),
            ZERO),
        new Sigma(le_i, boundI).normalize().simplify());

    var j = new BounderZ("j");
    var x_i_j = new Q("x", Set.of(i, j));
    var le_i_j = new Mono(3, x_i_j);
    var boundJ = new Bound(j, 10, 11);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Q("x[i:4][j:10]"), new Constant(3),
                new Q("x[i:5][j:10]"), new Constant(3),
                new Q("x[i:6][j:10]"), new Constant(3),
                new Q("x[i:4][j:11]"), new Constant(3),
                new Q("x[i:5][j:11]"), new Constant(3),
                new Q("x[i:6][j:11]"), new Constant(3)),
            ZERO),
        new Sigma(new Sigma(le_i_j, boundI), boundJ).normalize().simplify());
  }

  @Test
  public void nested_sigma() {
    var i = new BounderZ("i");
    var j = new BounderZ("j");
    var le = new Add(new Mono(2, i), new Mono(3, j));

    var boundI = new Bound(i, 4, 6);
    assertEquals(
        new NormalizedLE(Map.of(j, new Constant(9)), new Constant(30)),
        new Sigma(le, boundI).normalize().simplify());

    var boundJ = new Bound(j, 10, 11);
    assertEquals(
        new NormalizedLE(Map.of(), new Constant(249)),
        new Sigma(new Sigma(le, boundI), boundJ).normalize().simplify());
  }

  enum Days implements BounderValue {
    monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    public InstantiableE toArithmeticValue() throws ArithmeticConversionException {
      throw new ArithmeticConversionException("has no arith value");
    }
  }

  @Test
  public void weekend_as_bounder() {
    var weekend = new BounderZ("w");
    var weekend_bound = new Bound(weekend, saturday, sunday);

    var hours_weekend = new Z("hours", weekend);
    var hours_weekend_le = new Mono(hours_weekend);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Z("hours[w:saturday]"), ONE,
                new Z("hours[w:sunday]"), ONE),
            ZERO),
        new Sigma(hours_weekend_le, weekend_bound).normalize().simplify());

    var add_day_to_z = new Add(new Mono(weekend), hours_weekend_le);
    var e = assertThrows(
        RuntimeException.class,
        () -> new Sigma(add_day_to_z, weekend_bound).normalize());
    assertEquals(ArithmeticConversionException.class, e.getCause().getClass());
  }
}
