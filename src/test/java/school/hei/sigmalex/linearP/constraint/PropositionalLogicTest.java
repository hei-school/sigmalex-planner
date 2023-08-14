package school.hei.sigmalex.linearP.constraint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearP.solver.ORTools;
import school.hei.sigmalex.linearP.solver.Solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.geq;
import static school.hei.sigmalex.linearP.constraint.Constraint.or;
import static school.hei.sigmalex.linearP.constraint.False.FALSE;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public class PropositionalLogicTest {

  Solver subject;

  @BeforeEach
  public void setUp() {
    subject = new ORTools();
  }

  @Test
  public void true_and_p_is_p() {
    var p = geq(new Q("p"), 9);
    assertEquals(p.normalize(), and(TRUE, p).normalize());
    assertEquals(p.normalize(), and(p, TRUE).normalize());
  }

  @Test
  public void false_and_p_is_false() {
    var p = geq(new Q("p"), 9);
    assertEquals(FALSE.normalize(), and(FALSE, p).normalize());
    assertEquals(FALSE.normalize(), and(p, FALSE).normalize());
  }

  @Test
  public void true_or_p_is_true() {
    var p = geq(new Q("p"), 9);
    assertEquals(TRUE.normalize(), or(TRUE, p).normalize());
    assertEquals(TRUE.normalize(), or(p, TRUE).normalize());
  }

  @Test
  public void false_or_p_is_p() {
    var p = geq(new Q("p"), 9);
    assertEquals(p.normalize(), or(FALSE, p).normalize());
    assertEquals(p.normalize(), or(p, FALSE).normalize());
  }

  @Test
  public void distribute_and_over_or() {
    var a = geq(new Q("a"), 9);
    var b = geq(new Q("b"), 7);
    var c = geq(new Q("c"), 5);
    var d = geq(new Q("d"), 3);

    // a * (c+d) = a*c + a*d
    assertEquals(
        or(and(a, c), and(a, d)).normalize(),
        and(a, or(c, d)).normalize());
    // (a+b) * (c+d) = a*c + a*d + b*c + b*d
    assertEquals(
        or(and(a, c), and(a, d), and(b, c), and(b, d)).normalize(),
        and(or(a, b), or(c, d)).normalize());
  }
}
