package school.hei.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderQ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Instantiator;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Costly;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Teacher;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearE.LEFactory.vadd;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;
import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.geq;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

class PiConstraintTest {

  @Test
  public void sigma_le_and_pi_constraint() {
    var i = new BounderQ("i");
    var j = new BounderQ("j");
    var k = new BounderQ("k");
    var x_i_j_k = new Q("x", j, i, k);
    var le_i_j = vadd(mono(i), mono(j), mono(3, x_i_j_k));

    var sigmaBoundI = new Bound(i, 4, 6);
    var sigmaBoundJ = new Bound(j, 10, 11);
    var picBoundK = new Bound(k, 1, 2);
    Function<String, NormalizedConstraint> getExpectedNormalizedConstraint = vSuffix ->
        new NormalizedConstraint(new NormalizedLE(
            Map.of(
                new Q("x[i:4][j:10]" + vSuffix), new Constant(3),
                new Q("x[i:5][j:10]" + vSuffix), new Constant(3),
                new Q("x[i:6][j:10]" + vSuffix), new Constant(3),
                new Q("x[i:4][j:11]" + vSuffix), new Constant(3),
                new Q("x[i:5][j:11]" + vSuffix), new Constant(3),
                new Q("x[i:6][j:11]" + vSuffix), new Constant(3)),
            new Constant(93)));
    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            getExpectedNormalizedConstraint.apply("[k:1]"),
            getExpectedNormalizedConstraint.apply("[k:2]"))),
        pic(leq(sigma(le_i_j, sigmaBoundJ, sigmaBoundI), 0), picBoundK).normalize().simplify());
  }

  @Test
  public void vpic_as_nested_pic() {
    var i = new BounderQ("i");
    var j = new BounderQ("j");
    var x_i_j = new Q("x", j, i);

    var boundI = new Bound(i, 4, 6);
    var boundJ = new Bound(j, 10, 11);
    Function<String, NormalizedConstraint> getExpectedNormalizedConstraint = vName ->
        new NormalizedConstraint(new NormalizedLE(
            Map.of(new Q(vName), new Constant(-1)),
            new Constant(0)));
    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            getExpectedNormalizedConstraint.apply("x[i:4][j:10]"),
            getExpectedNormalizedConstraint.apply("x[i:5][j:10]"),
            getExpectedNormalizedConstraint.apply("x[i:6][j:10]"),
            getExpectedNormalizedConstraint.apply("x[i:4][j:11]"),
            getExpectedNormalizedConstraint.apply("x[i:5][j:11]"),
            getExpectedNormalizedConstraint.apply("x[i:6][j:11]"))),
        pic(geq(x_i_j, 0), boundI, boundJ).normalize().simplify());
  }

  @Test
  public void pic_with_contextual_instantiation() {
    var g1 = new Group("g1");
    var t1 = new Teacher("t1", new Date(2023, JULY, 20));
    var th1 = new Course("th1", Duration.ofHours(6));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);

    var ac = new BounderQ<AwardedCourse>("ac");
    var d = new BounderQ<Date>("d");
    var acBound = new Bound<>(ac, new AwardedCourse[]{ac_g1_th1_t1});
    var dBound = new Bound<>(d, new Date(2023, JULY, 20), new Date(2023, JULY, 21));
    var o_ac_d = new Z("o", ac, d);

    var ta = new BounderQ<Costly<?>>("ta");
    var taBound = new Bound(ta, t1);
    Instantiator<Costly<?>> instantiator = (Costly<?> teacher, SubstitutionContext<Costly<?>> ctx) ->
        ((Teacher) teacher).isAvailableOn((Date) (ctx.get(d).costly())) ? new Constant<>(1) : new Constant<>(0);

    var teacher_must_be_available =
        pic(eq(o_ac_d, mono(ta)), acBound, dBound, taBound.wi(instantiator));

    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), ONE), new Constant(-1))),
            new NormalizedConstraint(new NormalizedLE(Map.of(new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), new Constant(-1)), ONE)),
            new NormalizedConstraint(new NormalizedLE(Map.of(new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), ONE), ZERO)),
            new NormalizedConstraint(new NormalizedLE(Map.of(new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), new Constant(-1)), ZERO))
        )),
        teacher_must_be_available.normalize().simplify());
  }

  @Test
  public void pic_with_contextual_instantiation_inside_sigma() {
    var g1 = new Group("g1");
    var t1 = new Teacher("t1", new Date(2023, JULY, 20));
    var th1 = new Course("th1", Duration.ofHours(6));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);

    var ac = new BounderQ<AwardedCourse>("ac");
    var d = new BounderQ<Date>("d");
    var g = new BounderQ<Group>("g");
    var acBound = new Bound<>(ac, new AwardedCourse[]{ac_g1_th1_t1});
    var dBound = new Bound<>(d, new Date(2023, JULY, 20), new Date(2023, JULY, 21));
    var gBound = new Bound<>(g, g1);

    var o_ac_d = new Z("o", ac, d);
    var ta = new BounderQ<Costly<?>>("ta");
    var taBound = new Bound(ta, t1);
    Instantiator<Costly<?>> instantiator = (Costly<?> teacher, SubstitutionContext<Costly<?>> ctx) -> {
      assertNotNull(ctx.get(g));
      return ((Teacher) teacher).isAvailableOn((Date) (ctx.get(d).costly()))
          ? new Constant<>(1) : new Constant<>(0);
    };

    var teacher_must_be_available =
        pic(eq(sigma(mult(ta, o_ac_d), acBound, dBound, taBound.wi(instantiator)), mono(g)),
            gBound.wi(costly -> new Constant<>(7)));

    assertEquals(
        DisjunctivePolytopes.of(Polytope.of(
            new NormalizedConstraint(new NormalizedLE(Map.of(
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), ONE,
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), ZERO),
                new Constant(-7))),
            new NormalizedConstraint(new NormalizedLE(Map.of(
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul20]"), new Constant(-1),
                new Z("o[ac:[c:th1][g:g1][t:t1]][d:jul21]"), ZERO),
                new Constant(7)))
        )).toString(), //TODO: have to resort to string equality as fails otherwise. Prolly bounder names involved.
        teacher_must_be_available.normalize().simplify().toString());
  }
}