package school.hei.planner.constraint.sub;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderQ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Instantiator;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearP.constraint.Constraint;
import school.hei.planner.HEITimetable;
import school.hei.planner.constraint.HEITimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Costly;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Slot;

import java.util.Set;

import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.pic;

public class finish_course_hours_with_available_teachers extends HEITimetableConstraint {
  public finish_course_hours_with_available_teachers(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    var ta = new BounderQ<Costly<?>>("ta"); // teacher availability
    var taBound = new Bound<>(ta, () -> null);
    Instantiator<Costly<?>> taInstantiator = (Costly<?> costly, SubstitutionContext ctx) -> {
      var ctx_ac = (AwardedCourse) (ctx.get(ac).costly());
      var ctx_t = ctx_ac.teacher();
      var ctx_d = (Date) (ctx.get(d).costly());
      return ctx_t.isAvailableOn(ctx_d) ? new Constant<>(1) : new Constant<>(0);
    };
    var sh = Slot.DURATION.toHours();
    return pic(and(
            eq(ac, mult(sh, sigma(o_ac_d_s_r, dBound, sBound, rBound))),
            eq(ac, mult(sh, sigma(mult(ta, o_ac_d_s_r), dBound, sBound, rBound, taBound.wi(taInstantiator))))),
        acBound.wiq(AwardedCourse::durationInHours));
  }

  @Override
  public Set<String> violationRemedySuggestions() {
    return only_one_slot_max_per_course_per_day.TIME_VIOLATION_REMEDIES;
  }
}
