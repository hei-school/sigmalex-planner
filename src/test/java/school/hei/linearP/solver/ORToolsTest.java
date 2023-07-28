package school.hei.linearP.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.linearE.Add;
import school.hei.linearE.Mono;
import school.hei.linearE.Mult;
import school.hei.linearE.Sub;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.Solution;
import school.hei.linearP.constraint.Eq;
import school.hei.linearP.constraint.False;
import school.hei.linearP.constraint.Geq;
import school.hei.linearP.constraint.Leq;
import school.hei.linearP.constraint.True;
import school.hei.linearP.constraint.VariadicAnd;
import school.hei.linearP.constraint.VariadicOr;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.linearP.OptimizationType.max;
import static school.hei.linearP.OptimizationType.min;
import static school.hei.linearP.Solution.UNFEASIBLE;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.le;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.not;
import static school.hei.linearP.constraint.Constraint.or;
import static school.hei.linearP.constraint.False.FALSE;
import static school.hei.linearP.constraint.True.TRUE;

class ORToolsTest {

  Solver subject;

  @BeforeEach
  public void setUp() {
    subject = new ORTools();
  }

  @Test
  public void feasible_lp() {
    /* https://lpsolve.sourceforge.net/5.1/formulate.htm
       max: 143 x + 60 y;
       120 x + 200 y <= 15000 - 10 y;
       110 x + 30 y <= 4000;
       x + y <= 75; */

    var x = new Q("x");
    var y = new Q("y");
    var lp = new LP(
        max,
        new Add(
            new Mult(143, x), new Mult(60, y)),
        leq(
            new Add(new Mult(120, x), new Mult(200, y)),
            new Sub(15_000, new Mult(10, y))),
        leq(
            new Add(new Mult(110, x), new Mult(30, y)),
            4_000),
        leq(
            new Add(x, y), 75));
    assertEquals(
        new Solution(
            6315.625000000001,
            Map.of(
                x, 21.875,
                y, 53.12499999999999)),
        subject.solve(lp));
  }

  @Test
  public void feasible_ip_wikipedia() {
    /* https://en.wikipedia.org/wiki/Integer_programming
       Z+ x, y;
       max: y;
       - x + y <= 1;
       3 x + 2 y <= 12;
       2 x + 3 y <= 12; */
    var x = new Z("x");
    var y = new Z("y");
    var lp = new LP(
        max,
        y,
        new Geq(x, 0),
        new Geq(y, 0),
        leq(new Add(new Mult(-1, x), y), 1),
        leq(new Add(new Mult(3, x), 2), 12),
        leq(new Add(new Mult(2, x), new Mult(3, y)), 12));

    assertEquals(
        new Solution(
            2,
            Map.of(x, 1., y, 2.)),
        subject.solve(lp));
  }

  @Test
  public void feasible_ip_wikipedia_but_with_le() {
    /* https://en.wikipedia.org/wiki/Integer_programming
       Z+ x, y;
       max: y;
       - x + y < 1; // instead of <=
       3 x + 2 y <= 12;
       2 x + 3 y <= 12; */
    var x = new Z("x");
    var y = new Z("y");
    var lp = new LP(
        max,
        y,
        new Geq(x, 0),
        new Geq(y, 0),
        le(null, new Add(new Mult(-1, x), y), new Mono(1)), // instead of Leq
        leq(new Add(new Mult(3, x), 2), 12),
        leq(new Add(new Mult(2, x), new Mult(3, y)), 12));

    assertEquals(
        new Solution(
            2,
            Map.of(x, 2., y, 2.)),
        subject.solve(lp));
  }

  @Test
  public void feasible_ip_wikipedia_but_with_huge_epsilon_le() {
    /* https://en.wikipedia.org/wiki/Integer_programming
       Z+ x, y;
       max: y;
       - x + y < 1; // compiled into a <= with hugeEpsilon
       3 x + 2 y <= 12;
       2 x + 3 y <= 12; */
    var x = new Z("x");
    var y = new Z("y");
    var hugeEpsilon = 3;
    var lp = new LP(
        max,
        y,
        new Geq(x, 0),
        new Geq(y, 0),
        le( // instead of Leq
            null,
            new Add(new Mult(-1, x), y), new Mono(1),
            hugeEpsilon),
        leq(new Add(new Mult(3, x), 2), 12),
        leq(new Add(new Mult(2, x), new Mult(3, y)), 12));

    assertEquals(
        new Solution(
            1,
            Map.of(x, 3., y, 1.)),
        subject.solve(lp));
  }

  @Test
  public void ip_with_negated_constraints() {
    /* https://en.wikipedia.org/wiki/Integer_programming
       Z+ x, y;
       max: y;
       - x + y <= 1;    (a)
       3 x + 2 y <= 12; (b)
       2 x + 3 y <= 12; (c) */
    var x = new Z("x");
    var y = new Z("y");
    var x_domain = new Geq(x, 0);
    var y_domain = new Geq(y, 0);
    var a = leq(new Add(new Mult(-1, x), y), 1);
    var b = leq(new Add(new Mult(3, x), 2), 12);
    var c = leq(new Add(new Mult(2, x), new Mult(3, y)), 12);

    var feasible1 = new LP(
        max, y,
        x_domain, y_domain,
        not(a), b, c);
    assertEquals(
        new Solution(
            4,
            Map.of(x, -0., y, 4.)),
        subject.solve(feasible1));

    var feasible2 = new LP(
        max, y,
        x_domain, y_domain,
        a, not(b), c);
    assertEquals(
        new Solution(
            1,
            Map.of(x, 4., y, 1.)),
        subject.solve(feasible2));

    var unfeasible = new LP(
        max, y,
        x_domain, y_domain,
        not(a), not(b), c);
    assertEquals(UNFEASIBLE, subject.solve(unfeasible));

    var a_and_b_and_c = new LP(
        max, y,
        x_domain, y_domain,
        a, b, c);
    var a_and_b_and_c_but_in_a_fancy_way = new LP(
        max, y,
        x_domain, y_domain,
        not(not(a)), not(or(not(b), not(c))), // fancy a&b&c
        not(or(not(and(a, b)), FALSE)),  // fancy (redundant) a,b
        not(new False()), or(TRUE, not(new True())),
        not(or(not(and(new Eq(x, x), new Eq(x, x))), not(new Eq(x, x)))), // fancy x=x...
        not(and(not(or(new Eq(x, x), new Eq(x, x))), not(new Eq(x, x)))), // ...still
        not(new VariadicOr(not(new VariadicAnd(new Eq(x, x), new Eq(x, x))))), // ...as variadic
        not(new VariadicAnd(not(new VariadicOr(new Eq(x, x), new Eq(x, x)))))); // ...still!
    assertEquals(
        subject.solve(a_and_b_and_c),
        subject.solve(a_and_b_and_c_but_in_a_fancy_way));
  }

  @Test
  public void feasible_ip_wikipedia_as_fancy_propositional_constraints() {
    /* https://en.wikipedia.org/wiki/Integer_programming
       Z+ x, y;
       max: y;
       - x + y <= 1;    (a)
       3 x + 2 y <= 12; (b)
       2 x + 3 y <= 12; (c)
       If (a) is removed: objective optimal increases from 2 to 4 */
    var x = new Z("x");
    var y = new Z("y");
    var a = leq(new Add(new Mult(-1, x), y), 1);
    var b = leq(new Add(new Mult(3, x), 2), 12);
    var c = leq(new Add(new Mult(2, x), new Mult(3, y)), 12);

    var withGreaterObjective = new LP(
        max, y,
        new Geq(x, 0), new Geq(y, 0),
        b, c);
    var greaterSolution = subject.solve(withGreaterObjective);
    assertEquals(4, greaterSolution.optimalObjective());

    var withLesserObjective = new LP(
        max, y,
        new Geq(x, 0), new Geq(y, 0),
        a, c);
    var lesserSolution = subject.solve(withLesserObjective);
    assertEquals(2, lesserSolution.optimalObjective());

    var chooseBetweenAAndB = new LP(
        max, y,
        new Geq(x, 0), new Geq(y, 0),
        or(a, b), c);
    var chosenSolution = subject.solve(chooseBetweenAAndB);
    assertEquals(greaterSolution, chosenSolution);
  }

  @Test
  public void feasible_milp_mathworks() {
    /* https://fr.mathworks.com/help/optim/ug/intlinprog.html
       Q x1
       Z x2;
       max: 8 x1 + x2;
       x1 + 2 x2 >= -14;
       -4 x1 - x2 <= -33;
       2 x1 + 3 x2 <= 20; */
    var x1 = new Q("x1");
    var x2 = new Z("x2");
    var lp = new LP(
        min,
        new Add(new Mult(8, x1), x2),
        new Geq(
            new Add(x1, new Mult(2, x2)),
            -14),
        leq(
            new Sub(new Mult(-4, x1), x2),
            -33),
        leq(
            new Add(new Mult(2, x1), x2),
            20));

    var solution = subject.solve(lp);

    assertFalse(solution.isEmpty());
    assertEquals(
        new Solution(
            59,
            Map.of(
                x1, 6.5,
                x2, 7.)),
        solution);
  }

  @Test
  public void feasible_milp_mathworks_as_fancy_propositional_constraints() {
    /* https://fr.mathworks.com/help/optim/ug/intlinprog.html
       Q x1; Z x2;
       max: 8 x1 + x2;
       x1 + 2 x2 >= -14;  (a)
       -4 x1 - x2 <= -33; (b)
       2 x1 + 3 x2 <= 20; (c)
       Feasible iff: a | b&c
       That is:
         * unfeasible if (a) only, or (b) only, or (c) only
         * unfeasible if (a) and (b) only, or (a) and (c) only, or (b) and (c)
         * feasible if (a) and (b) and (c)... that we will let SigmaLex finds through a | b&c */
    var x1 = new Q("x1");
    var x2 = new Z("x2");
    var objective = new Add(new Mult(8, x1), x2);
    var a = new Geq(new Add(x1, new Mult(2, x2)), -14);
    var b = leq(new Add(new Mult(2, x1), x2), 20);
    var c = leq(new Sub(new Mult(-4, x1), x2), -33);

    var unfeasible1 = new LP(min, objective, a);
    assertTrue(subject.solve(unfeasible1).isEmpty());

    var unfeasible2 = new LP(min, objective, Set.of(a, b));
    assertTrue(subject.solve(unfeasible2).isEmpty());

    var unfeasible3 = new LP(min, objective, Set.of(a, c));
    assertTrue(subject.solve(unfeasible3).isEmpty());

    var feasible = new LP(min, objective, or(a, and(b, c)));
    assertEquals(
        new Solution(58.99999999999999, Map.of(x1, 6.500000000000002, x2, 7.)),
        subject.solve(feasible));
  }

  @Test
  public void unfeasible() {
    var lp = new LP(
        min,
        new Q("x"),
        new Geq(0, 1));

    var solution = subject.solve(lp);

    assertTrue(solution.isEmpty());
    assertEquals(UNFEASIBLE, solution);
  }
}