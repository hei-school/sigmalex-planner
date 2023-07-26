package school.hei.linearP;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Add;
import school.hei.linearE.Mono;
import school.hei.linearE.NormalizedLE;
import school.hei.linearE.Sub;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.constraint.Eq;
import school.hei.linearP.constraint.Geq;
import school.hei.linearP.constraint.Leq;
import school.hei.linearP.constraint.NormalizedConstraint;
import school.hei.linearP.constraint.VariadicAnd;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.instantiableE.Constant.ZERO;
import static school.hei.linearP.OptimizationType.max;
import static school.hei.linearP.OptimizationType.min;

class LPTest {

  @Test
  public void min_leq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var lpName = "lp_name";
    var cName = "constraint_name";

    assertEquals(
        Set.of(new NormalizedLP(
            lpName,
            min,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        ZERO))))),
        new LP(
            lpName,
            min,
            new Add(new Mono(x), new Mono(7)),
            Set.of(
                new Leq(cName, new Mono(x), new Mono(y))))
            .normalize());
  }

  @Test
  public void min_geq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var lpName = "lp_name";
    var cName = "constraint_name";

    assertEquals(
        Set.of(new NormalizedLP(
            lpName,
            min,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        ZERO))))),
        new LP(
            lpName,
            min,
            new Add(new Mono(x), new Mono(7)),
            Set.of(
                new Geq(cName, new Mono(x), new Mono(y))))
            .normalize());
  }

  @Test
  public void max_eq_constraint() {
    var x = new Q("x");
    var y = new Q("y");
    var lpName = "lp_name";
    var cName = "constraint_name";

    assertEquals(
        Set.of(new NormalizedLP(
            lpName,
            max,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        new Constant(9))),
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        new Constant(-9)))))),
        new LP(
            lpName,
            max,
            new Add(new Mono(x), new Mono(7)),
            Set.of(
                new Eq(cName, new Mono(x), new Sub(new Mono(y), new Mono(9)))))
            .normalize());
  }

  @Test
  public void max_eq_constraint_as_variadic() {
    var x = new Q("x");
    var y = new Q("y");
    var lpName = "lp_name";
    var cName = "constraint_name";

    Sub yPlus9 = new Sub(new Mono(y), new Mono(9));
    assertEquals(
        Set.of(new NormalizedLP(
            lpName,
            max,
            new NormalizedLE(Map.of(x, new Constant(1)), new Constant(7)),
            Set.of(
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(1),
                            y, new Constant(-1)),
                        new Constant(9))),
                new NormalizedConstraint(
                    cName,
                    new NormalizedLE(
                        Map.of(
                            x, new Constant(-1),
                            y, new Constant(1)),
                        new Constant(-9)))))),
        new LP(
            lpName,
            max,
            new Add(new Mono(x), new Mono(7)),
            Set.of(
                new VariadicAnd(
                    cName,
                    new Leq(cName, new Mono(x), yPlus9),
                    new Geq(cName, new Mono(x), yPlus9))))
            .normalize());
  }
}