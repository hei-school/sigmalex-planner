package school.hei.sigmalex.linearE;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.exception.DuplicateVariableNameException;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.Z;
import school.hei.sigmalex.linearE.instantiableE.exception.NoDuplicateBounderException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.sigmalex.linearE.LEFactory.add;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearE.LEFactory.vadd;

class VariadicSigmaTest {
  @Test
  public void nested_sigma_as_variadic() {
    var i = new BounderQ<>("i");
    var j = new BounderQ<>("j");
    var le = add(mono(2, i), mono(3, j));

    var boundI = new Bound<>(i, 4, 6);
    var boundJ = new Bound<>(j, 10, 11);
    assertEquals(
        sigma(sigma(le, boundI), boundJ).normify(),
        sigma(le, boundI, boundJ).normify());
  }

  @Test
  public void bounded_vars() {
    var i = new BounderQ<>("i");
    var j = new BounderQ<>("j");
    var x_i_j = new Q("x", j, i);
    var le_i_j = vadd(mono(i), mono(j), mono(3, x_i_j));

    var boundI = new Bound<>(i, 4, 6);
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
            new Constant<>(93)),
        sigma(le_i_j, boundJ, boundI).normify());
  }

  @Test
  public void duplicate_bounders_prohibited() {
    var i = new BounderQ<>("i");
    assertThrows(NoDuplicateBounderException.class, () -> new Q("x", i, i));
    assertThrows(NoDuplicateBounderException.class, () -> new Z("x", i, i));
  }

  @Test
  public void duplicate_names_prohibited() {
    var x = new Z("x");
    var y = new Q("x"); // oopsie
    assertThrows(
        DuplicateVariableNameException.class,
        () -> add(mono(3, x), mono(2.5, y)).normify());

    var i = new BounderQ<>("i");
    var j = new BounderQ<>("j");
    var x_i_j = new Z("x", j, i);
    var y_i_j = new Q("x", j, i); // oopsie
    var le_i_j = vadd(x_i_j, y_i_j);
    var boundI = new Bound<>(i, 4, 6);
    var boundJ = new Bound<>(j, 10, 11);
    assertThrows(
        DuplicateVariableNameException.class,
        () -> sigma(le_i_j, boundI, boundJ).normify());

    var exactly_x = new Z("x");
    var normified =
        // In previous lines, we could detect that x and y cannot be mixed in an LE
        // as they have same name but different types.
        // In following lines, we cannot do that as the obtained LE still makes sense:
        // it could be that for the user wanted to
        // refer to the same Z variable through two different ways.
        add(mono(3, x), mono(2.5, exactly_x))
            .normify();
    assertNotNull(normified);
  }
}