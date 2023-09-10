package school.hei.planner.constraint.sub.constant;

import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.sigmalex.linearP.constraint.Constraint;

import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.forall;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;

public class only_one_slot_max_per_course_per_day extends TimetableConstraint {

  public static final Set<String> TIME_VIOLATION_REMEDIES = Set.of(
      "Increase school days",
      "Increase teacher availabilities",
      "Decrease course hours");

  public only_one_slot_max_per_course_per_day(Timetable timetable, boolean withExpConstraints) {
    super(timetable, withExpConstraints);
  }

  @Override
  public Constraint constraint() {
    return forall(leq(sigma(o_ac_d_s_r, sBound, rBound), 1), acBound, dBound);
  }

  @Override
  public Set<String> violationRemedySuggestions() {
    return TIME_VIOLATION_REMEDIES;
  }
}
