package school.hei.linearP.hei;

import org.junit.jupiter.api.Test;
import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.solver.ORTools;

import java.util.Map;
import java.util.regex.Pattern;

import static java.time.Month.JULY;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearE.instantiableE.IEFactory.addie;
import static school.hei.linearE.instantiableE.IEFactory.multie;
import static school.hei.linearP.OptimizationType.min;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.geq;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

public class HEITest {

  private static final Pattern OCCUPATION_VAR_PATTERN = Pattern.compile(
      "occupation\\[c:(.*)\\]\\[d:(.*)\\]\\[g:(.*)\\]\\[r:(.*)\\]\\[s:(.*)\\]");

  @Test
  public void ask_sigmalex_the_wise_to_plan_hei() {
    var c = new BounderZ("c");
    var g = new BounderZ("g");
    var d = new BounderZ("d");
    var s = new BounderZ("s");
    var r = new BounderZ("r");
    Map<String, BounderZ> bounders = Map.of("c", c, "g", g, "d", d, "s", s, "r", r);

    var cBound = new Bound(c, Course.values());
    var gBound = new Bound(g, Group.values());
    var dBound = new Bound(d, new Date[]{
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25)});
    var sBound = new Bound(s, Slot.values());
    var rBound = new Bound(r, Room.values());
    var bounds = Map.of("c", cBound, "g", gBound, "d", dBound, "s", sBound, "r", rBound);

    var prioritize_early_days_and_slots_context = prioritize_early_days_and_slots(bounders, bounds);
    var days_off = new Date[]{
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22)};
    var lp = new LP(
        min, prioritize_early_days_and_slots_context.objective,
        prioritize_early_days_and_slots_context.constraint,
        exclude_days_off(days_off, bounders, bounds),
        only_one_slot_max_per_course_per_day(bounders, bounds),
        finish_courses_without_room_conflict(bounders, bounds));

    var actual_solution = new ORTools().solve(lp);
    assertEquals(9744240, actual_solution.optimalObjective());
    assertEquals(
        """
            occupation[c:prog2][d:jul20][g:g1][r:a][s:f08t10]=1.0
            occupation[c:th1][d:jul20][g:g2][r:b][s:f08t10]=1.0
            occupation[c:th1][d:jul20][g:g1][r:a][s:f10t12]=1.0
            occupation[c:prog2][d:jul20][g:g2][r:b][s:f10t12]=1.0
            occupation[c:prog2][d:jul23][g:g1][r:a][s:f08t10]=1.0
            occupation[c:th1][d:jul23][g:g2][r:b][s:f08t10]=1.0
            occupation[c:prog2][d:jul23][g:g2][r:a][s:f10t12]=1.0
            occupation[c:th1][d:jul23][g:g1][r:b][s:f10t12]=1.0
            occupation[c:prog2][d:jul24][g:g1][r:a][s:f08t10]=1.0
            occupation[c:th1][d:jul24][g:g2][r:b][s:f08t10]=1.0
            occupation[c:prog2][d:jul24][g:g2][r:a][s:f10t12]=1.0
            occupation[c:th1][d:jul24][g:g1][r:b][s:f10t12]=1.0
            occupation[c:prog2][d:jul25][g:g2][r:a][s:f08t10]=1.0
            occupation[c:prog2][d:jul25][g:g1][r:b][s:f08t10]=1.0
            occupation[c:th1][d:jul25][g:g2][r:a][s:f10t12]=1.0
            occupation[c:th1][d:jul25][g:g1][r:b][s:f10t12]=1.0""",
        toHEIPlanning(actual_solution.optimalBoundedVariablesForUnboundedName("occupation")));
  }

  private String toHEIPlanning(Map<String, Double> occupations) {
    return occupations.entrySet().stream()
        .sorted(this::compareOccupationEntry)
        .map(Map.Entry::toString)
        .collect(joining("\n"));
  }

  private int compareOccupationEntry(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
    var name1 = entry1.getKey();
    var matcher1 = OCCUPATION_VAR_PATTERN.matcher(name1);
    if (!matcher1.find()) {
      throw new RuntimeException("Variable name does not follow expected pattern: " + name1);
    }

    var name2 = entry2.getKey();
    var matcher2 = OCCUPATION_VAR_PATTERN.matcher(name2);
    if (!matcher2.find()) {
      throw new RuntimeException("Variable name does not follow expected pattern: " + name2);
    }

    var compareDates = matcher1.group(2).compareTo(matcher2.group(2));
    if (compareDates != 0) {
      return compareDates;
    }
    var compareSlots = matcher1.group(5).compareTo(matcher2.group(5));
    if (compareSlots != 0) {
      return compareSlots;
    }
    var compareRooms = matcher1.group(4).compareTo(matcher2.group(4));
    return compareRooms;
  }

  private Constraint only_one_slot_max_per_course_per_day(Map<String, BounderZ> bounders, Map<String, Bound> bounds) {
    var c = bounders.get("c");
    var g = bounders.get("g");
    var d = bounders.get("d");
    var s = bounders.get("s");
    var r = bounders.get("r");

    var cBound = bounds.get("c");
    var gBound = bounds.get("g");
    var dBound = bounds.get("d");
    var sBound = bounds.get("s");
    var rBound = bounds.get("r");

    var o_c_d_g_s_r = new Z("occupation", c, d, g, s, r);
    return pic(leq(sigma(o_c_d_g_s_r, sBound, rBound), 1), cBound, dBound, gBound);
  }

  private Constraint exclude_days_off(Date[] off, Map<String, BounderZ> bounders, Map<String, Bound> bounds) {
    var c = bounders.get("c");
    var g = bounders.get("g");
    var d = bounders.get("d");
    var s = bounders.get("s");
    var r = bounders.get("r");

    var cBound = bounds.get("c");
    var gBound = bounds.get("g");
    var dBound = new Bound(d, off);
    var sBound = bounds.get("s");
    var rBound = bounds.get("r");

    var o_c_d_g_s_r = new Z("occupation", c, d, g, s, r);
    return pic(eq(o_c_d_g_s_r, 0), cBound, dBound, sBound, gBound, rBound);
  }

  private LPContext prioritize_early_days_and_slots(Map<String, BounderZ> bounders, Map<String, Bound> bounds) {
    var c = bounders.get("c");
    var g = bounders.get("g");
    var d = bounders.get("d");
    var s = bounders.get("s");
    var r = bounders.get("r");

    var cBound = bounds.get("c");
    var gBound = bounds.get("g");
    var dBound = bounds.get("d");
    var sBound = bounds.get("s");
    var rBound = bounds.get("r");

    var o_c_d_g_s_r = new Z("occupation", c, d, g, s, r);

    var cost_d_s = new Q("cost", d, s);
    var cost_c_d_g_s_r = new Q("cost", c, d, g, s, r);
    var cost_domains = and(
        pic(geq(cost_d_s, 0), dBound, sBound),
        pic(geq(cost_c_d_g_s_r, 0), dBound, sBound, rBound));

    var cost = sigma(cost_c_d_g_s_r, cBound, dBound, gBound, sBound, rBound);
    var s_per_day = new Constant(Slot.values().length);
    var cost_d_s_unlinked_to_o = addie(multie(d, s_per_day), s);
    var assign_costs =
        pic(eq(cost_c_d_g_s_r, mult(cost_d_s_unlinked_to_o, o_c_d_g_s_r)), cBound, dBound, gBound, sBound, rBound);
    return new LPContext(cost, and(cost_domains, assign_costs));
  }

  private Constraint finish_courses_without_room_conflict(Map<String, BounderZ> bounders, Map<String, Bound> bounds) {
    var c = bounders.get("c");
    var g = bounders.get("g");
    var d = bounders.get("d");
    var s = bounders.get("s");
    var r = bounders.get("r");

    var cBound = bounds.get("c");
    var gBound = bounds.get("g");
    var dBound = bounds.get("d");
    var sBound = bounds.get("s");
    var rBound = bounds.get("r");

    var o_c_d_g_s_r = new Z("occupation", c, d, g, s, r);
    var o_d_g_s_r = new Z("o", d, g, s, r);
    var o_d_s_r = new Z("o", d, s, r);
    var t_c_g = new Z("t", c, g); // time
    var o_and_t_domains = and(
        pic(geq(t_c_g, 0), cBound, gBound),
        pic(and(leq(0, o_c_d_g_s_r), leq(o_c_d_g_s_r, 1)), cBound, dBound, gBound, sBound, rBound),
        pic(and(leq(0, o_d_g_s_r), leq(o_d_g_s_r, 1)), dBound, gBound, sBound, rBound),
        pic(and(leq(0, o_d_s_r), leq(o_d_s_r, 1)), dBound, sBound, rBound));
    var st = Slot.DURATION.toHours();
    var ct = Course.DURATION.toHours();
    var finish_courses = and(
        pic(eq(t_c_g, mult(st, sigma(o_c_d_g_s_r, dBound, sBound, rBound))), cBound, gBound),
        pic(eq(t_c_g, ct), cBound, gBound));
    var room_is_occupied_when_a_group_studies_there =
        pic(eq(o_d_s_r, sigma(o_c_d_g_s_r, cBound, gBound)), dBound, sBound, rBound);
    var a_group_can_only_study_a_course_at_a_time =
        pic(leq(sigma(o_c_d_g_s_r, cBound, rBound), 1), dBound, sBound, gBound);

    return and(
        o_and_t_domains,
        finish_courses,
        room_is_occupied_when_a_group_studies_there,
        a_group_can_only_study_a_course_at_a_time);
  }

  record LPContext(LinearE objective, Constraint constraint) {
  }
}
