package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.linearE.LEFactory.add;
import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;

class SigmaTest {
  @Test
  public void first_n_sum() {
    var k = new BounderZ("k");
    int n = 10;
    assertEquals(
        new NormalizedLE(n * (n + 1) / 2.),
        new Sigma(mono(k), new Bound(k, 1, n)).normalize().simplify());
  }

  @Test
  public void first_arith_progression_sum() {
    // Does Wikipedia know math? https://en.wikipedia.org/wiki/Arithmetic_progression
    int n = 5, a = 2, d = 3;
    var k = new BounderZ("k");

    assertEquals(
        new NormalizedLE(n / 2. * (2 * a + (n - 1) * d)),
        new Sigma(
            add(mono(a), mult(d, add(mono(k), mono(-1)))),
            new Bound(k, 1, n))
            .normalize().simplify());
  }

  @Test
  public void bounded_vars() {
    var i = new BounderZ("i");
    var x_i = new Q("x", Set.of(i));
    var le_i = mono(3, x_i);

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
    var le_i_j = mono(3, x_i_j);
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
    var le = add(mono(2, i), mono(3, j));

    var boundI = new Bound(i, 4, 6);
    assertEquals(
        new NormalizedLE(Map.of(j, new Constant(9)), new Constant(30)),
        new Sigma(le, boundI).normalize().simplify());

    var boundJ = new Bound(j, 10, 11);
    assertEquals(
        new NormalizedLE(Map.of(), new Constant(249)),
        new Sigma(new Sigma(le, boundI), boundJ).normalize().simplify());
  }

  @Test
  public void weekend_as_bounder() {
    var weekend = new BounderZ("w");
    var weekend_bound = new Bound(weekend, NonInstantiableDays.saturday, NonInstantiableDays.sunday);

    var hours_weekend = new Z("hours", weekend);
    var hours_weekend_le = mono(hours_weekend);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Z("hours[w:saturday]"), ONE,
                new Z("hours[w:sunday]"), ONE),
            ZERO),
        new Sigma(hours_weekend_le, weekend_bound).normalize().simplify());

    var add_day_to_z = add(mono(weekend), hours_weekend_le);
    var e = assertThrows(
        RuntimeException.class,
        () -> new Sigma(add_day_to_z, weekend_bound).normalize());
    assertEquals(ArithmeticConversionException.class, e.getCause().getClass());
  }

  @Test
  public void weekend_as_bounder_with_dynamic_instantiation() {
    var weekend = new BounderZ<InstantiableDays>("w");
    var weekend_bound_with_dynamic_instantiation = new Bound(
        weekend.wi(day -> new Constant(day.order)),
        InstantiableDays.saturday, InstantiableDays.sunday);

    var hours_weekend = new Z("hours", weekend);
    var hours_weekend_le = mono(hours_weekend);
    var add_day_to_z = add(mono(weekend), hours_weekend_le);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Z("hours[w:saturday]"), ONE,
                new Z("hours[w:sunday]"), ONE),
            new Constant(13)),
        new Sigma(add_day_to_z, weekend_bound_with_dynamic_instantiation).normalize().simplify());
  }

  enum NonInstantiableDays implements BounderValue<Void> {
    monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    public Void costly() {
      return null;
    }

    @Override
    public InstantiableE<Void> toQ(Void v, Function<Void, InstantiableE<Void>> instantiator) throws ArithmeticConversionException {
      throw new ArithmeticConversionException("Days is not supposed to be instantiated");
    }
  }

  enum InstantiableDays implements BounderValue<InstantiableDays> {
    saturday(6), sunday(7);

    private final int order;

    InstantiableDays(int order) {
      this.order = order;
    }

    @Override
    public InstantiableDays costly() {
      return this;
    }

    @Override
    public InstantiableE<InstantiableDays> toQ(
        InstantiableDays costly, Function<InstantiableDays, InstantiableE<InstantiableDays>> instantiator)
        throws ArithmeticConversionException {
      return instantiator.apply(costly);
    }
  }
}
