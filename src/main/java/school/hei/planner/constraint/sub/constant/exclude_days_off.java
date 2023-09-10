package school.hei.planner.constraint.sub.constant;

import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.sigmalex.linearP.constraint.Constraint;

import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.forall;

public class exclude_days_off extends TimetableConstraint {
  public exclude_days_off(Timetable timetable, boolean withExpConstraints) {
    super(timetable, withExpConstraints);
  }

  @Override
  public Constraint constraint() {
    return forall(eq(o_ac_d_s_r, 0), acBound, doffBound, sBound, rBound);
  }
}
