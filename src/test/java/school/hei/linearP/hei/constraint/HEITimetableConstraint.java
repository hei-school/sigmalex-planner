package school.hei.linearP.hei.constraint;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderQ;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.MILP;
import school.hei.linearP.Solution;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.constraint.sub.a_group_can_only_study_a_course_at_a_time;
import school.hei.linearP.hei.constraint.sub.exclude_days_off;
import school.hei.linearP.hei.constraint.sub.finish_course_hours_with_available_teachers;
import school.hei.linearP.hei.constraint.sub.no_group_studies_all_day_long;
import school.hei.linearP.hei.constraint.sub.only_one_slot_max_per_course_per_day;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.solver.ORTools;

import java.util.List;

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

public class HEITimetableConstraint implements SATConstraint {

  protected final HEITimetable timetable;
  protected final BounderQ<AwardedCourse> ac = new BounderQ<>("ac");
  protected final BounderQ<Date> d = new BounderQ<>("d");

  protected final BounderQ<Slot> s = new BounderQ<>("s");
  protected final BounderQ<Room> r = new BounderQ<>("r");
  protected final Bound<AwardedCourse> acBound;
  protected final Bound<Date> dBound;
  protected final Bound<Date> doffBound;

  protected final Bound<Slot> sBound;
  protected final Bound<Room> rBound;

  protected final Z o_ac_d_s_r;

  public HEITimetableConstraint(HEITimetable timetable) {
    this.timetable = timetable;
    this.acBound = new Bound<>(ac, timetable.awarded_courses());
    this.dBound = new Bound<>(d, timetable.dates_all());
    this.doffBound = new Bound<>(d, timetable.dates_off());
    this.sBound = new Bound<>(s, timetable.slots());
    this.rBound = new Bound<>(r, timetable.rooms());
    this.o_ac_d_s_r = new Z("occupation", ac, d, s, r);
  }

  protected MILPContext prioritize_early_days_and_slots() {
    var cost_d_s = new Q("cost", d, s);
    var cost_ac_d_s_r = new Q("cost", ac, d, s, r);
    var cost_domains = and(
        pic(geq(cost_d_s, 0), dBound, sBound),
        pic(geq(cost_ac_d_s_r, 0), dBound, sBound, rBound));

    var cost = sigma(cost_ac_d_s_r, acBound, dBound, sBound, rBound);
    var cost_d_s_unlinked_to_o = addie(multie(d, Slot.SLOTS_IN_A_DAY), s);
    var assign_costs =
        pic(eq(cost_ac_d_s_r, mult(cost_d_s_unlinked_to_o, o_ac_d_s_r)),
            acBound, dBound.wiq(Date::cost), sBound.wiq(Slot::cost), rBound);
    return new MILPContext(cost, and(cost_domains, assign_costs));
  }

  public Solution solve() {
    var domains =
        pic(and(leq(0, o_ac_d_s_r), leq(o_ac_d_s_r, 1)), acBound, dBound, sBound, rBound);
    var prioritize_early_days_and_slots_context = prioritize_early_days_and_slots();
    var milp = new MILP(
        min, prioritize_early_days_and_slots_context.objective(),
        prioritize_early_days_and_slots_context.constraint(),
        domains, constraint());
    return new ORTools().solve(milp);
  }

  private List<SATConstraint> subConstraints() {
    return List.of(
        new exclude_days_off(timetable),
        new only_one_slot_max_per_course_per_day(timetable),
        new no_group_studies_all_day_long(timetable),
        new a_group_can_only_study_a_course_at_a_time(timetable),
        new finish_course_hours_with_available_teachers(timetable));
  }

  @Override
  public Constraint constraint() {
    return and(subConstraints().stream()
        .map(SATConstraint::constraint)
        .toArray(Constraint[]::new));
  }
}
