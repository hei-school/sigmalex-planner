package school.hei.linearE;

import org.junit.jupiter.api.Test;
import school.hei.linearE.exception.DuplicateVariableNameException;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearE.instantiableE.exception.NoDuplicateBounderException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.linearE.LEFactory.add;
import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearE.LEFactory.vadd;
import static school.hei.linearE.LEFactory.sigma;

class VariadicSigmaTest {
  @Test
  public void nested_sigma_as_variadic() {
    var i = new BounderZ("i");
    var j = new BounderZ("j");
    var le = add(mono(2, i), mono(3, j));

    var boundI = new Bound(i, 4, 6);
    var boundJ = new Bound(j, 10, 11);
    assertEquals(
        new Sigma(new Sigma(le, boundI), boundJ).normalize(),
        sigma(le, boundI, boundJ).normalize());
  }

  @Test
  public void bounded_vars() {
    var i = new BounderZ("i");
    var j = new BounderZ("j");
    var x_i_j = new Q("x", j, i);
    var le_i_j = vadd(mono(i), mono(j), mono(3, x_i_j));

    var boundI = new Bound(i, 4, 6);
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
            new Constant(93)),
        sigma(le_i_j, boundJ, boundI).normalize().simplify());
  }

  @Test
  public void duplicate_bounders_prohibited() {
    var i = new BounderZ("i");
    assertThrows(NoDuplicateBounderException.class, () -> new Q("x", i, i));
    assertThrows(NoDuplicateBounderException.class, () -> new Z("x", i, i));
  }

  @Test
  public void duplicate_names_prohibited() {
    var x = new Z("x");
    var y = new Q("x"); // oopsie
    assertThrows(
        DuplicateVariableNameException.class,
        () -> add(mono(3, x), mono(2.5, y)).normalize().simplify());

    var i = new BounderZ("i");
    var j = new BounderZ("j");
    var x_i_j = new Z("x", j, i);
    var y_i_j = new Q("x", j, i); // oopsie
    var le_i_j = vadd(x_i_j, y_i_j);
    var boundI = new Bound(i, 4, 6);
    var boundJ = new Bound(j, 10, 11);
    assertThrows(
        DuplicateVariableNameException.class,
        () -> sigma(le_i_j, boundI, boundJ).normalize());

    var exactly_x = new Z("x");
    var normified =
        // In previous lines, we could detect that x and y cannot be mixed in an LE
        // as they have same name but different types.
        // In following lines, we cannot do that as the obtained LE still makes sense:
        // it could be that for the user wanted to
        // refer to the same Z variable through two different ways.
        add(mono(3, x), mono(2.5, exactly_x))
            .normalize().simplify();
    assertNotNull(normified);
  }
}