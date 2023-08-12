package school.hei.linearP.hei.constraint.sub;

import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;

import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

public class only_one_slot_max_per_course_per_day extends HEITimetableConstraint {
  public only_one_slot_max_per_course_per_day(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    return pic(leq(sigma(o_ac_d_s_r, sBound, rBound), 1), acBound, dBound);
  }
}
