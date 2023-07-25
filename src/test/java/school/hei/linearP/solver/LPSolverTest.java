package school.hei.linearP.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.linearE.Add;
import school.hei.linearE.Mono;
import school.hei.linearE.Mult;
import school.hei.linearE.Sub;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.constraint.LP;
import school.hei.linearP.constraint.Leq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearP.OptimizationType.max;

class LPSolverTest {

  Solver subject;

  @BeforeEach
  public void setUp() {
    subject = new LPSolver();
  }

  @Test
  public void feasible1() {
    /* max: 143 x + 60 y;
       120 x + 200 y <= 15000 - 10 y;
       110 x + 30 y <= 4000;
       x + y <= 75"""; */

    var x = new Q("x");
    var y = new Q("y");
    var lp = new LP(
        max,
        new Add(new Mult(143, x), new Mult(60, y)),
        new Leq(
            new Add(new Mult(120, x), new Mult(200, y)),
            new Sub(new Mono(15_000), new Mult(10, y))),
        new Leq(
            new Add(new Mult(110, x), new Mult(30, y)),
            new Mono(4_000)),
        new Leq(
            new Add(x, y),
            new Mono(75)));
    assertEquals("""
            Value of objective function: 6315.62500000
                    
            Actual values of the variables:
            y                          53.125
            x                          21.875""",
        subject.solve(lp.normalize()).toString());
  }
}