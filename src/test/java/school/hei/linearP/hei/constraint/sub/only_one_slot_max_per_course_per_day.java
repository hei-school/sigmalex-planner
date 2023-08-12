package school.hei.linearP.hei.constraint.sub;

import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;

import java.util.Set;

import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;

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
