package school.hei.linearP.hei;

import org.junit.jupiter.api.Test;
import school.hei.linearE.Mult;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.solver.ORTools;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static school.hei.linearE.LEFactory.vsigma;
import static school.hei.linearP.OptimizationType.min;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.equiv;
import static school.hei.linearP.constraint.Constraint.geq;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;
import static school.hei.linearP.constraint.Constraint.vand;

public class HEITest {

  enum Day implements BounderValue {
    mon, tue//, wed, thu, fri, sat, sun
  }

  enum Hour implements BounderValue {
    from8_to10, from10_to12;//, from13_to15, from15_to17;

    static final Duration t = Duration.ofHours(2);
  }

  enum Course implements BounderValue {
    th1;//, prog2, sys2, web2;

    static final Duration t = Duration.ofHours(8);
  }

  enum Group implements BounderValue {
    h1, h2//, h3, h4
  }

  enum Room implements BounderValue {
    a, b
  }

  @Test
  public void finish_courses_without_room_conflict() {
    var c = new BounderZ("c"); //TODO: not BounderZ
    var g = new BounderZ("g");
    var d = new BounderZ("d");
    var h = new BounderZ("h");

    var cBound = new Bound(c, Course.values());
    var gBound = new Bound(g, Group.values());
    var dBound = new Bound(d, Day.values());
    var hBound = new Bound(h, Hour.values());

    var t_c_g = new Z("t", c, g);
    var r_c_d_g_h = new Z("r", c, d, g, h); //TODO: not Z
    var r_d_g_h = new Z("r", d, g, h);
    var var_domains = vand(
        pic(geq(t_c_g, 0), cBound, gBound),
        pic(and(leq(0, r_c_d_g_h), leq(r_c_d_g_h, 1)), cBound, dBound, gBound, hBound),
        pic(and(leq(0, r_d_g_h), leq(r_d_g_h, 1)), dBound, gBound, hBound));

    var ht = Hour.t.toHours();
    var ct = Course.t.toHours();

    var finish_courses = and(
        eq(t_c_g, new Mult(ht, vsigma(r_c_d_g_h, cBound, dBound, gBound, hBound))),
        eq(t_c_g, ct));
    var room_occupied_iff_a_group_uses_it = equiv(
        eq(r_c_d_g_h, 1),
        eq(r_d_g_h, 1));
    var room_can_only_host_a_group_max = pic(
        leq(vsigma(r_d_g_h, gBound), 1),
        dBound, hBound);

    var lp = new LP(
        min, t_c_g,
        var_domains,
        finish_courses,
        room_occupied_iff_a_group_uses_it,
        room_can_only_host_a_group_max);
    var solution = new ORTools().solve(lp);
    assertFalse(solution.isEmpty());
  }
}
