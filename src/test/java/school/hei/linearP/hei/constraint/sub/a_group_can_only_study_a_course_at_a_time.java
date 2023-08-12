package school.hei.linearP.hei.constraint.sub;

import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;

import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

public class a_group_can_only_study_a_course_at_a_time extends HEITimetableConstraint {
  public a_group_can_only_study_a_course_at_a_time(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    return and(
        pic(leq(sigma(o_ac_d_s_r, rBound), 1), acBound, dBound, sBound),
        pic(leq(sigma(o_ac_d_s_r, acBound), 1), rBound, dBound, sBound));
  }
}
