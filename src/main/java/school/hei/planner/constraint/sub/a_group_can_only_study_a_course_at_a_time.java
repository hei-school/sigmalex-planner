package school.hei.planner.constraint.sub;

import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.sigmalex.linearP.constraint.Constraint;

import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;
import static school.hei.sigmalex.linearP.constraint.Constraint.pic;

public class a_group_can_only_study_a_course_at_a_time extends TimetableConstraint {
  public a_group_can_only_study_a_course_at_a_time(Timetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    return and(
        pic(leq(sigma(mult(g, o_ac_d_s_r), acBound, rBound), 1), gBound.wi(if_ac()), dBound, sBound),
        pic(leq(sigma(o_ac_d_s_r, acBound), 1), rBound, dBound, sBound));
  }
}
