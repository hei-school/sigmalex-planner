package school.hei.linearP.hei;

import org.junit.jupiter.api.Test;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.solver.ORTools;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.LEFactory.mult;
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
    mon, tue, wed
  }

  enum Hour implements BounderValue {
    from8_to10, from10_to12, from13_to15;

    static final Duration t = Duration.ofHours(2);
  }

  enum Course implements BounderValue {
    th1, prog2;

    static final Duration t = Duration.ofHours(8);
  }

  enum Group implements BounderValue {
    h1, h2
  }

  enum Room implements BounderValue {
    a, b
  }

  @Test
  public void finish_courses_without_room_conflict() {
    var c = new BounderZ("c");
    var g = new BounderZ("g");
    var d = new BounderZ("d");
    var h = new BounderZ("h");
    var r = new BounderZ("r");

    var cBound = new Bound(c, Course.values());
    var gBound = new Bound(g, Group.values());
    var dBound = new Bound(d, Day.values());
    var hBound = new Bound(h, Hour.values());
    var rBound = new Bound(r, Room.values());

    var o_c_d_g_h_r = new Z("occupation", c, d, g, h, r);
    var t_c_g = new Z("t", c, g);
    var o_d_g_h_r = new Z("o", d, g, h, r);
    var o_d_h_r = new Z("o", d, h, r);
    var var_domains = vand(
        pic(geq(t_c_g, 0), cBound, gBound),
        pic(and(leq(0, o_c_d_g_h_r), leq(o_c_d_g_h_r, 1)), cBound, dBound, gBound, hBound, rBound),
        pic(and(leq(0, o_d_g_h_r), leq(o_d_g_h_r, 1)), dBound, gBound, hBound, rBound),
        pic(and(leq(0, o_d_h_r), leq(o_d_h_r, 1)), dBound, hBound, rBound));

    var ht = Hour.t.toHours();
    var ct = Course.t.toHours();
    var finish_courses = vand(
        pic(eq(t_c_g, mult(ht, vsigma(o_c_d_g_h_r, dBound, hBound, rBound))), cBound, gBound),
        pic(eq(t_c_g, ct), cBound, gBound));
    var room_is_occupied_when_a_group_studies_there =
        pic(eq(o_d_h_r, vsigma(o_c_d_g_h_r, cBound, gBound)), dBound, hBound, rBound);
    var a_group_can_only_study_a_course_at_a_time =
        pic(leq(vsigma(o_c_d_g_h_r, cBound, rBound), 1), dBound, hBound, gBound);

    var lp = new LP(
        min, t_c_g,
        var_domains,
        finish_courses,
        room_is_occupied_when_a_group_studies_there,
        a_group_can_only_study_a_course_at_a_time,
        equiv( // superfluous constraint, just to verify that equiv generates disjunctive polytopes
            a_group_can_only_study_a_course_at_a_time,
            room_is_occupied_when_a_group_studies_there));

    var actual_solution = new ORTools().solve(lp);
    Map<String, Double> expected_solution = new HashMap<>();
    //-------------------------------- MONDAY ---------------------------------------//
    expected_solution.put("occupation[c:th1][d:mon][g:h1][h:from8_to10][r:a]", 1.);
    expected_solution.put("occupation[c:prog2][d:mon][g:h2][h:from8_to10][r:b]", 1.);

    expected_solution.put("occupation[c:th1][d:mon][g:h1][h:from10_to12][r:a]", 1.);
    expected_solution.put("occupation[c:prog2][d:mon][g:h2][h:from10_to12][r:b]", 1.);

    expected_solution.put("occupation[c:th1][d:mon][g:h2][h:from13_to15][r:a]", 1.);
    expected_solution.put("occupation[c:prog2][d:mon][g:h1][h:from13_to15][r:b]", 1.);

    //-------------------------------- TUESDAY ---------------------------------------//
    expected_solution.put("occupation[c:th1][d:tue][g:h2][h:from8_to10][r:a]", 1.);
    expected_solution.put("occupation[c:prog2][d:tue][g:h1][h:from8_to10][r:b]", 1.);

    expected_solution.put("occupation[c:prog2][d:tue][g:h2][h:from10_to12][r:a]", 1.);

    expected_solution.put("occupation[c:th1][d:tue][g:h1][h:from13_to15][r:a]", 1.);
    expected_solution.put("occupation[c:prog2][d:tue][g:h2][h:from13_to15][r:b]", 1.);

    //-------------------------------- WEDNESDAY -------------------------------------//
    expected_solution.put("occupation[c:prog2][d:wed][g:h1][h:from8_to10][r:a]", 1.);
    expected_solution.put("occupation[c:th1][d:wed][g:h2][h:from8_to10][r:b]", 1.);

    expected_solution.put("occupation[c:th1][d:wed][g:h1][h:from10_to12][r:a]", 1.);
    expected_solution.put("occupation[c:th1][d:wed][g:h2][h:from10_to12][r:b]", 1.);

    expected_solution.put("occupation[c:prog2][d:wed][g:h1][h:from13_to15][r:b]", 1.);
    //------------------------------------------ -------------------------------------//
    assertEquals(
        expected_solution,
        actual_solution.optimalBoundedVariablesForUnboundedName("occupation"));
  }
}
