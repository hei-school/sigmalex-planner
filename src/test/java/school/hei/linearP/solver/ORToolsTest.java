package school.hei.linearP.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.linearE.Add;
import school.hei.linearE.Mult;
import school.hei.linearE.Sub;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.LP;
import school.hei.linearP.Solution;
import school.hei.linearP.constraint.Leq;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearP.OptimizationType.max;

class ORToolsTest {

  Solver subject;

  @BeforeEach
  public void setUp() {
    subject = new ORTools();
  }

  @Test
  public void feasible1() {
    /* max: 143 x + 60 y;
       120 x + 200 y <= 15000 - 10 y;
       110 x + 30 y <= 4000;
       x + y <= 75; */

    var x = new Q("x");
    var y = new Q("y");
    var lp = new LP(
        max,
        new Add(
            new Mult(143, x), new Mult(60, y)),
        new Leq(
            new Add(new Mult(120, x), new Mult(200, y)),
            new Sub(15_000, new Mult(10, y))),
        new Leq(
            new Add(new Mult(110, x), new Mult(30, y)),
            4_000),
        new Leq(
            new Add(x, y), 75));
    assertEquals(
        new Solution(
            6315.624999993015,
            Map.of(
                x, 21.875,
                y, 53.124999999883585)),
        subject.solve(lp));
  }
}