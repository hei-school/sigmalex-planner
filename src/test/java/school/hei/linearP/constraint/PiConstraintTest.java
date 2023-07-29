package school.hei.linearP.constraint;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Mono;
import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearP.constraint.polytope.DisjunctivePolytopes;
import school.hei.linearP.constraint.polytope.Polytope;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.LEFactory.vadd;
import static school.hei.linearE.LEFactory.vsigma;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

class PiConstraintTest {

  @Test
  public void sigma_le_and_pi_constraint() {
    var i = new BounderZ("i");
    var j = new BounderZ("j");
    var k = new BounderZ("k");
    var x_i_j_k = new Q("x", j, i, k);
    var le_i_j = vadd(new Mono(i), new Mono(j), new Mono(3, x_i_j_k));

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
        pic(leq(vsigma(le_i_j, sigmaBoundJ, sigmaBoundI), 0), picBoundK).normalize());
  }

}