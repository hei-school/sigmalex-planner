package school.hei.linearP.hei;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderQ;
import school.hei.linearE.instantiableE.Instantiator;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.LP;
import school.hei.linearP.Solution;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Costly;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.solver.ORTools;

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

public class HEITimetable {

  private final LP milp;

  public HEITimetable(AwardedCourse[] awarded_courses, Room[] rooms, Date[] dates_all, Date[] dates_off, Slot[] slots) {
    var ac = new BounderQ<AwardedCourse>("ac");
    var d = new BounderQ<Date>("d");
    var s = new BounderQ<Slot>("s");
    var r = new BounderQ<Room>("r");
    var cBound = new Bound<>(ac, awarded_courses);
    var dBound = new Bound<>(d, dates_all);
    var sBound = new Bound<>(s, slots);
    var rBound = new Bound<>(r, rooms);
    var prioritize_early_days_and_slots_context =
        prioritize_early_days_and_slots(ac, d, s, r, cBound, dBound, sBound, rBound);
    this.milp = new LP(
        min, prioritize_early_days_and_slots_context.objective,
        prioritize_early_days_and_slots_context.constraint,
        exclude_days_off(dates_off, ac, d, s, r, cBound, sBound, rBound),
        only_one_slot_max_per_course_per_day(ac, d, s, r, cBound, dBound, sBound, rBound),
        finish_course_hours_with_available_teachers_and_no_room_conflict(ac, d, s, r, cBound, dBound, sBound, rBound));
  }

  private Constraint only_one_slot_max_per_course_per_day(
      BounderQ<AwardedCourse> ac, BounderQ<Date> d, BounderQ<Slot> s, BounderQ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Date> dBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    return pic(leq(sigma(o_ac_d_s_r, sBound, rBound), 1), acBound, dBound);
  }

  private Constraint exclude_days_off(
      Date[] off,
      BounderQ<AwardedCourse> ac, BounderQ<Date> d, BounderQ<Slot> s, BounderQ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var dBound = new Bound<>(d, off);
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    return pic(eq(o_ac_d_s_r, 0), acBound, dBound, sBound, rBound);
  }

  private LPContext prioritize_early_days_and_slots(
      BounderQ<AwardedCourse> ac, BounderQ<Date> d, BounderQ<Slot> s, BounderQ<Room> r,
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
      BounderQ<AwardedCourse> ac, BounderQ<Date> d, BounderQ<Slot> s, BounderQ<Room> r,
      Bound<AwardedCourse> acBound, Bound<Date> dBound, Bound<Slot> sBound, Bound<Room> rBound) {
    var o_ac_d_s_r = new Z<>("occupation", ac, d, s, r);
    var o_domain =
        pic(and(leq(0, o_ac_d_s_r), leq(o_ac_d_s_r, 1)), acBound, dBound, sBound, rBound);

    var ta = new BounderQ<Costly>("ta"); // teacher availability
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

  public Solution solve() {
    return new ORTools().solve(milp);
  }

  record LPContext(LinearE objective, Constraint constraint) {
  }
}
