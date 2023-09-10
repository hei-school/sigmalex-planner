package school.hei.sigmalex.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.planner.costly.Date;
import school.hei.sigmalex.linearE.NormalizedLE;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.sigmalex.linearP.constraint.polytope.Polytope;

import java.util.Map;
import java.util.Set;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.linearE.instantiableE.Bound.bound;
import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.forall;
import static school.hei.sigmalex.linearP.constraint.Constraint.or;

class OrTest {

  @Test
  void or_in_pic_1() {
    var d1 = new BounderQ<Date>("d1");
    var d2 = new BounderQ<>("d2");
    var d1Bound = bound(d1, new Date(2023, JULY, 20));
    var d2Bound = bound(d2, new Date(2023, JULY, 21));
    var constraint = forall(or(
            eq(d1, 0),
            eq(d2, 0)),
        d1Bound.wiq(costly -> 1.), d2Bound.wiq(costly -> 1.));

    var normified = constraint.normify();

    assertEquals(
        new DisjunctivePolytopes(Set.of(
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-1))))))),
        normified);
  }


  @Test
  void or_in_pic_2() {
    var d1 = new BounderQ<Date>("d1");
    var d2 = new BounderQ<>("d2");
    var d1Bound = bound(d1, new Date(2023, JULY, 20));
    var d2Bound = bound(d2, new Date(2023, JULY, 21));
    var constraint = forall(or(
            eq(d1, 0),
            eq(d2, 0)),
        d1Bound.wiq(costly -> 1.), d2Bound.wiq(costly -> 2.));

    var normified = constraint.normify();

    assertEquals(
        new DisjunctivePolytopes(Set.of(
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-1))))),
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(2))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-2))))))),
        normified);
  }

  @Test
  void or_in_pic_3() {
    var d1 = new BounderQ<Date>("d1");
    var d2 = new BounderQ<>("d2");
    var d1Bound = bound(d1, new Date(2023, JULY, 20));
    var d2Bound = bound(d2, new Date(2023, JULY, 21), new Date(2023, JULY, 22));
    var constraint = forall(or(
            eq(d1, 0),
            eq(d2, 0)),
        d1Bound.wiq(costly -> 1.), d2Bound.wiq(costly -> (double) ((Date) costly).getLocalDate().getDayOfMonth()));

    var normified = constraint.normify();

    // (d1 + d21) (d1 + d22) = d1 + d1 d21 + d1 d22 + d21 d22
    assertEquals(
        new DisjunctivePolytopes(Set.of(
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-1))))),
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(21))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-21))))),
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-1))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(22))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-22))))),
            new Polytope(Set.of(
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(21))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-21))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(22))),
                new NormalizedConstraint(new NormalizedLE(Map.of(), new Constant<>(-22))))))),
        normified);
  }
}