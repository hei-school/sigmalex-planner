package school.hei.linearP.hei;

import org.junit.jupiter.api.RepeatedTest;
import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderZ;
import school.hei.linearE.instantiableE.Instantiator;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Costly;
import school.hei.linearP.hei.costly.Course;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.hei.costly.Teacher;
import school.hei.linearP.solver.ORTools;

import java.time.Duration;
import java.util.Map;
import java.util.regex.Pattern;

import static java.time.Month.JULY;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearE.instantiableE.Constant.ONE;
import static school.hei.linearE.instantiableE.Constant.ZERO;
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
      "occupation\\[ac:\\[c:(.*)]\\[g:(.*)]\\[t:(.*)]]\\[d:(.*)]\\[r:(.*)]\\[s:(.*)]");

  @RepeatedTest(value = 10)
  public void ask_sigmalex_the_wise_to_plan_hei() {
    var g1 = new Group("g1");
    var g2 = new Group("g2");
    var t1 = new Teacher(
        "t1",
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25));
    var t2 = new Teacher(
        "t2",
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25));
    var th1 = new Course("th1", Duration.ofHours(6));
    var prog2 = new Course("prog2", Duration.ofHours(8));
    var sem1 = new Course("sem1", Duration.ofHours(2));
    var ac_g1_th1_t1 = new AwardedCourse(th1, g1, t1);
    var ac_g2_th1_t1 = new AwardedCourse(th1, g2, t1);
    var ac_g1_prog2_t2 = new AwardedCourse(prog2, g1, t2);
    var ac_g2_prog2_t2 = new AwardedCourse(prog2, g2, t2);
    var ac_g1_sem1_t2 = new AwardedCourse(sem1, g1, t2);
    var awarded_courses = new AwardedCourse[]{
        ac_g1_th1_t1, ac_g2_th1_t1, ac_g1_prog2_t2, ac_g2_prog2_t2, ac_g1_sem1_t2};
    var ra = new Room("a");
    var rb = new Room("b");
    var rooms = new Room[]{ra, rb};
    var dates_all = new Date[]{
        new Date(2023, JULY, 20),
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22),
        new Date(2023, JULY, 23),
        new Date(2023, JULY, 24),
        new Date(2023, JULY, 25)};
    var dates_off = new Date[]{ // to be enriched later on with slots_off
        new Date(2023, JULY, 21),
        new Date(2023, JULY, 22)};
    Slot.DURATION = Duration.ofHours(2);
    Slot.SLOTS_IN_A_DAY = 4;
    var f08t10 = new Slot("f08t10", 1);
    var f10t12 = new Slot("f10t12", 2);
    var f13t15 = new Slot("f13t15", 10);
    var f15t17 = new Slot("f15t17", 20);
    var slots = new Slot[]{f08t10, f10t12, f13t15, f15t17};

    var ac = new BounderZ<AwardedCourse>("ac");
    var d = new BounderZ<Date>("d");
    var s = new BounderZ<Slot>("s");
    var r = new BounderZ<Room>("r");
    var cBound = new Bound<>(ac, awarded_courses);
    var dBound = new Bound<>(d, dates_all);
    var sBound = new Bound<>(s, slots);
    var rBound = new Bound<>(r, rooms);
    var prioritize_early_days_and_slots_context =
        prioritize_early_days_and_slots(ac, d, s, r, cBound, dBound, sBound, rBound);
    var lp = new LP(
        min, prioritize_early_days_and_slots_context.objective,
        prioritize_early_days_and_slots_context.constraint,
        exclude_days_off(dates_off, ac, d, s, r, cBound, sBound, rBound),
        only_one_slot_max_per_course_per_day(ac, d, s, r, cBound, dBound, sBound, rBound),
        finish_course_hours_with_available_teachers_and_no_room_conflict(ac, d, s, r, cBound, dBound, sBound, rBound));

    var actual_solution = new ORTools().solve(lp);
    assertEquals(1.219312E7, actual_solution.optimalObjective());
    assertEquals(
        """
            occupation[ac:[c:sem1][g:g1][t:t2]][d:jul20][r:b][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul20][r:a][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul20][r:a][s:f13t15]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul23][r:a][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul23][r:a][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul23][r:a][s:f13t15]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul23][r:b][s:f15t17]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul24][r:b][s:f08t10]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul24][r:a][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul24][r:a][s:f13t15]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul24][r:a][s:f15t17]=1.0
            occupation[ac:[c:prog2][g:g1][t:t2]][d:jul25][r:a][s:f08t10]=1.0
            occupation[ac:[c:th1][g:g2][t:t1]][d:jul25][r:a][s:f10t12]=1.0
            occupation[ac:[c:prog2][g:g2][t:t2]][d:jul25][r:a][s:f13t15]=1.0
            occupation[ac:[c:th1][g:g1][t:t1]][d:jul25][r:a][s:f15t17]=1.0""",
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

    var compareDates = matcher1.group(4).compareTo(matcher2.group(4));
    if (compareDates != 0) {
      return compareDates;
    }
    var compareSlots = matcher1.group(6).compareTo(matcher2.group(6));
    if (compareSlots != 0) {
      return compareSlots;
    }
    return matcher1.group(5).compareTo(matcher2.group(5)); // room
  }

  private Constraint only_one_slot_max_per_course_per_day(
      BounderZ<AwardedCourse> ac, BounderZ<Date> d, BounderZ<Slot> s, BounderZ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Date> dBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    return pic(leq(sigma(o_ac_d_s_r, sBound, rBound), 1), acBound, dBound);
  }

  private Constraint exclude_days_off(
      Date[] off,
      BounderZ<AwardedCourse> ac, BounderZ<Date> d, BounderZ<Slot> s, BounderZ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var dBound = new Bound<>(d, off);
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    return pic(eq(o_ac_d_s_r, 0), acBound, dBound, sBound, rBound);
  }

  private LPContext prioritize_early_days_and_slots(
      BounderZ<AwardedCourse> ac, BounderZ<Date> d, BounderZ<Slot> s, BounderZ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Date> dBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);

    var cost_d_s = new Q<>("cost", d, s);
    var cost_ac_d_s_r = new Q<>("cost", ac, d, s, r);
    var cost_domains = and(
        pic(geq(cost_d_s, 0), dBound, sBound),
        pic(geq(cost_ac_d_s_r, 0), dBound, sBound, rBound));

    var cost = sigma(cost_ac_d_s_r, acBound, dBound, sBound, rBound);
    var cost_d_s_unlinked_to_o = addie(multie(d, Slot.SLOTS_IN_A_DAY), s);
    var assign_costs =
        pic(eq(cost_ac_d_s_r, mult(cost_d_s_unlinked_to_o, o_ac_d_s_r)),
            acBound, dBound.wiq(Date::cost), sBound.wiq(Slot::cost), rBound);
    return new LPContext(cost, and(cost_domains, assign_costs));
  }

  private Constraint finish_course_hours_with_available_teachers_and_no_room_conflict(
      BounderZ<AwardedCourse> ac, BounderZ<Date> d, BounderZ<Slot> s, BounderZ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Date> dBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    var o_domain =
        pic(and(leq(0, o_ac_d_s_r), leq(o_ac_d_s_r, 1)), acBound, dBound, sBound, rBound);

    var ta = new BounderZ<Costly>("ta"); // teacher availability
    var taBound = new Bound<>(ta, new Costly());
    Instantiator<Costly> taInstantiator = (costly, ctx) -> {
      var lambda_ac = (AwardedCourse) (ctx.get(ac).costly());
      var lambda_t = lambda_ac.teacher();
      var lambda_d = (Date) (ctx.get(d).costly());
      return lambda_t.isAvailableOn(lambda_d) ? ONE : ZERO;
    };
    var sh = Slot.DURATION.toHours();
    var finish_courses_hours_with_teacher =
        pic(eq(ac, mult(sh, sigma(mult(ta, o_ac_d_s_r), dBound, sBound, rBound, taBound.wi(taInstantiator)))),
            acBound.wiq(AwardedCourse::durationInHours));

    var a_group_can_only_study_a_course_at_a_time =
        pic(leq(sigma(o_ac_d_s_r, acBound, rBound), 1), dBound, sBound);

    return and(
        o_domain,
        finish_courses_hours_with_teacher,
        a_group_can_only_study_a_course_at_a_time);
  }

  record LPContext(LinearE objective, Constraint constraint) {
  }
}
