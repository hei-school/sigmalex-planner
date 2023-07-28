package school.hei.linearP.constraint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.solver.ORTools;
import school.hei.linearP.solver.Solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

public class PropositionalLogicTest {

  Solver subject;

  @BeforeEach
  public void setUp() {
    subject = new ORTools();
  }

  @Test
  public void true_and_p_is_p() {
    var p = new Geq(new Q("p"), 9);
    assertEquals(p.normalize(), and(TRUE, p).normalize());
    assertEquals(p.normalize(), and(p, TRUE).normalize());
  }

  @Test
  public void false_and_p_is_false() {
    var p = new Geq(new Q("p"), 9);
    assertEquals(FALSE.normalize(), and(FALSE, p).normalize());
    assertEquals(FALSE.normalize(), and(p, FALSE).normalize());
  }

  @Test
  public void true_or_p_is_true() {
    var p = new Geq(new Q("p"), 9);
    assertEquals(TRUE.normalize(), new Or(TRUE, p).normalize());
    assertEquals(TRUE.normalize(), new Or(p, TRUE).normalize());
  }

  @Test
  public void false_or_p_is_p() {
    var p = new Geq(new Q("p"), 9);
    assertEquals(p.normalize(), new Or(FALSE, p).normalize());
    assertEquals(p.normalize(), new Or(p, FALSE).normalize());
  }

  @Test
  public void distribute_and_over_or() {
    var a = new Geq(new Q("a"), 9);
    var b = new Geq(new Q("b"), 7);
    var c = new Geq(new Q("c"), 5);
    var d = new Geq(new Q("d"), 3);

    // a * (c+d) = a*c + a*d
    assertEquals(
        new VariadicOr(and(a, c), and(a, d)).normalize(),
        and(a, new Or(c, d)).normalize());
    // (a+b) * (c+d) = a*c + a*d + b*c + b*d
    assertEquals(
        new VariadicOr(and(a, c), and(a, d), and(b, c), and(b, d)).normalize(),
        and(new Or(a, b), new Or(c, d)).normalize());
  }
}
