package school.hei.sigmalex.linearP;

import org.junit.jupiter.api.Test;
import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearP.constraint.NormalizedConstraint;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.LEFactory.add;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearE.LEFactory.sub;
import static school.hei.sigmalex.linearE.instantiableE.Constant.ZERO;
import static school.hei.sigmalex.linearP.OptimizationType.max;
import static school.hei.sigmalex.linearP.OptimizationType.min;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.geq;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;

class MILPTest {

  @Test
  public void min_leq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var milpName = "lp_name";

    assertEquals(
        Set.of(new NormalizedMILP(
            milpName, min,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        ZERO))))),
        new MILP(
            milpName, min,
            add(mono(x), mono(7)),
            Set.of(leq(mono(x), mono(y))))
            .normify());
  }

  @Test
  public void min_geq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var milpName = "lp_name";

    assertEquals(
        Set.of(new NormalizedMILP(
            milpName, min,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        ZERO))))),
        new MILP(
            milpName, min,
            add(mono(x), mono(7)),
            Set.of(
                geq(mono(x), mono(y))))
            .normify());
  }

  @Test
  public void max_eq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var milpName = "lp_name";

    assertEquals(
        Set.of(new NormalizedMILP(
            milpName, max,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        new Constant(9))),
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        new Constant(-9)))))),
        new MILP(
            milpName, max,
            add(mono(x), mono(7)),
            Set.of(eq(mono(x), sub(mono(y), mono(9)))))
            .normify());
  }

  @Test
  public void max_eq_constraint_as_variadic() {
    var x = new Q("x");
    var y = new Q("y");
    var milpName = "lp_name";

    var yPlus9 = sub(mono(y), mono(9));
    assertEquals(
        Set.of(new NormalizedMILP(
            milpName, max,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        new Constant(9))),
                new NormalizedConstraint(
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        new Constant(-9)))))),
        new MILP(
            milpName, max,
            add(mono(x), mono(7)),
            Set.of(
                and(
                    leq(mono(x), yPlus9),
                    geq(mono(x), yPlus9))))
            .normify());
  }
}