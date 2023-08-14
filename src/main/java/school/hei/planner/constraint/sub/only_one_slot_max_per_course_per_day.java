package school.hei.planner.constraint.sub;

import school.hei.sigmalex.linearP.constraint.Constraint;
import school.hei.planner.HEITimetable;
import school.hei.planner.constraint.HEITimetableConstraint;

import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;
import static school.hei.sigmalex.linearP.constraint.Constraint.pic;

public class only_one_slot_max_per_course_per_day extends HEITimetableConstraint {

  public static final Set<String> TIME_VIOLATION_REMEDIES = Set.of(
      "Increase school days",
      "Increase teacher availabilities",
      "Decrease course hours");

  public only_one_slot_max_per_course_per_day(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    return pic(leq(sigma(o_ac_d_s_r, sBound, rBound), 1), acBound, dBound);
  }

  @Override
  public Set<String> violationRemedySuggestions() {
    return TIME_VIOLATION_REMEDIES;
  }
}
