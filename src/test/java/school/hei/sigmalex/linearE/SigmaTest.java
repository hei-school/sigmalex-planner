package school.hei.sigmalex.linearE;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.BounderValue;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.Instantiator;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Z;
import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.sigmalex.linearE.LEFactory.add;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ONE;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.multie;

class SigmaTest {
  @Test
  public void first_n_sum() {
    var k = new BounderQ<>("k");
    int n = 10;
    assertEquals(
        new NormalizedLE(n * (n + 1) / 2.),
        sigma(mono(k), new Bound<>(k, 1, n)).subnormify());
  }

  @Test
  public void first_arith_progression_sum() {
    // Does Wikipedia know math? https://en.wikipedia.org/wiki/Arithmetic_progression
    int n = 5, a = 2, d = 3;
    var k = new BounderQ<>("k");

    assertEquals(
        new NormalizedLE(n / 2. * (2 * a + (n - 1) * d)),
        sigma(
            add(mono(a), mult(d, add(mono(k), mono(-1)))),
            new Bound<>(k, 1, n))
            .subnormify());
  }

  @Test
  public void bounded_vars() {
    var i = new BounderQ<>("i");
    var x_i = new Q("x", Set.of(i));
    var le_i = mono(3, x_i);

    var boundI = new Bound<>(i, 4, 6);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Q("x[i:4]"), new Constant<>(3),
                new Q("x[i:5]"), new Constant<>(3),
                new Q("x[i:6]"), new Constant<>(3)),
            ZERO),
        sigma(le_i, boundI).subnormify());

    var j = new BounderQ<>("j");
    var x_i_j = new Q("x", Set.of(i, j));
    var le_i_j = mono(3, x_i_j);
    var boundJ = new Bound<>(j, 10, 11);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Q("x[i:4][j:10]"), new Constant<>(3),
                new Q("x[i:5][j:10]"), new Constant<>(3),
                new Q("x[i:6][j:10]"), new Constant<>(3),
                new Q("x[i:4][j:11]"), new Constant<>(3),
                new Q("x[i:5][j:11]"), new Constant<>(3),
                new Q("x[i:6][j:11]"), new Constant<>(3)),
            ZERO),
        sigma(sigma(le_i_j, boundI), boundJ).subnormify());
  }

  @Test
  public void nested_sigma() {
    var i = new BounderQ<>("i");
    var j = new BounderQ<>("j");
    var le = add(mono(2, i), mono(3, j));

    var boundI = new Bound<>(i, 4, 6);
    assertEquals(
        new NormalizedLE(Map.of(j, new Constant<>(9)), new Constant<>(30)),
        sigma(le, boundI).subnormify());

    var boundJ = new Bound<>(j, 10, 11);
    assertEquals(
        new NormalizedLE(Map.of(), new Constant<>(249)),
        sigma(sigma(le, boundI), boundJ).subnormify());
  }

  @Test
  public void weekend_as_bounder() {
    var weekend = new BounderQ<NonInstantiableDays>("w");
    var weekend_bound = new Bound<>(weekend, NonInstantiableDays.saturday, NonInstantiableDays.sunday);

    var hours_weekend = new Z("hours", weekend);
    var hours_weekend_le = mono(hours_weekend);
    assertEquals(
        new NormalizedLE(
            Map.of(
                new Z("hours[w:saturday]"), ONE,
                new Z("hours[w:sunday]"), ONE),
            ZERO),
        sigma(hours_weekend_le, weekend_bound).subnormify());

    var add_day_to_z = add(mono(weekend), hours_weekend_le);
    var e = assertThrows(
        RuntimeException.class,
        () -> sigma(add_day_to_z, weekend_bound).subnormify());
    assertTrue(e.getCause() instanceof ArithmeticConversionException);
  }

  @Test
  public void weekend_as_bounder_with_dynamic_instantiation() {
    var weekend = new BounderQ<InstantiableDays>("w");
    var hours_weekend = new Z("hours", weekend);
    var weekend_bound = new Bound<>(weekend, InstantiableDays.saturday, InstantiableDays.sunday);

    assertEquals(
        new NormalizedLE(
            Map.of(
                new Z("hours[w:saturday]"), ONE,
                new Z("hours[w:sunday]"), ONE),
            new Constant<>(26)),
        sigma(add(mono(hours_weekend), mono(weekend)),
            weekend_bound.wi(day -> multie(2, day.order)))
            .subnormify());
  }

  @Test
  public void weekend_as_bounder_with_nested_dynamic_instantiation() {
    var weekend = new BounderQ<InstantiableDays>("w");
    var working_day = new BounderQ<InstantiableDays>("d");
    var weekend_bound = new Bound<>(weekend, InstantiableDays.saturday, InstantiableDays.sunday);
    var working_bound = new Bound<>(working_day, InstantiableDays.monday);

    var le = add(mono(working_day), mono(weekend));
    var correctly_ordered_bounds = new Bound[]{
        working_bound.wi(day -> multie(2, weekend)),
        weekend_bound.wiq(day -> 3. * day.order)};
    assertEquals(
        new NormalizedLE(Map.of(), new Constant<>(117)),
        sigma(le, correctly_ordered_bounds).subnormify());
  }

  @Test
  public void weekend_as_bounder_with_nested_contextual_instantiation() {
    var weekend = new BounderQ<InstantiableDays>("w");
    var working_day = new BounderQ<InstantiableDays>("d");
    var weekend_bound = new Bound<>(weekend, InstantiableDays.saturday, InstantiableDays.sunday);
    var working_bound = new Bound<>(working_day, InstantiableDays.monday);

    var le = add(mono(working_day), mono(weekend));
    Instantiator<InstantiableDays> contextual_wi = (day, ctx) -> {
      var w_as_costly = ((InstantiableDays) (ctx.get(weekend).costly()));
      return new Constant<>(w_as_costly.order + 3. * day.order);
    };
    var correctly_ordered_bounds = new Bound[]{
        working_bound.wi(day -> multie(2, weekend)),
        weekend_bound.wi(contextual_wi)};
    assertEquals(
        new NormalizedLE(Map.of(), new Constant<>(156)),
        sigma(le, correctly_ordered_bounds).subnormify());
  }

  enum NonInstantiableDays implements BounderValue<NonInstantiableDays> {
    monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    @Override
    public NonInstantiableDays costly() {
      return null;
    }

    @Override
    public InstantiableE<NonInstantiableDays> toQ(
        NonInstantiableDays costly,
        SubstitutionContext substitutionContext,
        Instantiator<NonInstantiableDays> instantiator)
        throws ArithmeticConversionException {
      throw new ArithmeticConversionException("Days is not supposed to be instantiated");
    }
  }

  enum InstantiableDays implements BounderValue<InstantiableDays> {
    monday(1), saturday(6), sunday(7);

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
        InstantiableDays costly,
        SubstitutionContext substitutionContext,
        Instantiator<InstantiableDays> instantiator)
        throws ArithmeticConversionException {
      return instantiator.apply(costly, substitutionContext);
    }
  }
}
