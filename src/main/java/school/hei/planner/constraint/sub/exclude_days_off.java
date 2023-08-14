package school.hei.planner.constraint.sub;

import school.hei.linearP.constraint.Constraint;
import school.hei.planner.HEITimetable;
import school.hei.planner.constraint.HEITimetableConstraint;

import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.pic;

public class exclude_days_off extends HEITimetableConstraint {
  public exclude_days_off(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    return pic(eq(o_ac_d_s_r, 0), acBound, doffBound, sBound, rBound);
  }
}
