package school.hei.planner.constraint.sub;

import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Costly;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Slot;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Instantiator;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.Constraint;

import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.pic;

public class finish_course_hours_with_available_teachers extends TimetableConstraint {
  public finish_course_hours_with_available_teachers(Timetable timetable) {
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
        acBound.wiq(ac -> ac.durationInHours() + 0.));
  }

  @Override
  public Set<String> violationRemedySuggestions() {
    return only_one_slot_max_per_course_per_day.TIME_VIOLATION_REMEDIES;
  }
}
