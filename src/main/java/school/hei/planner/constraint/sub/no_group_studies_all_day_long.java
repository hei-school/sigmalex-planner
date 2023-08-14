package school.hei.planner.constraint.sub;

import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearP.constraint.Constraint;
import school.hei.planner.HEITimetable;
import school.hei.planner.constraint.HEITimetableConstraint;
import school.hei.planner.costly.Slot;

import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;
import static school.hei.sigmalex.linearP.constraint.Constraint.pic;

public class no_group_studies_all_day_long extends HEITimetableConstraint {
  public no_group_studies_all_day_long(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    var s1s4Bound = new Bound<>(s, new Slot[]{Slot.f08t10, Slot.f15t17});
    return pic(leq(sigma(mult(g, o_ac_d_s_r), s1s4Bound, acBound), 1), gBound.wi(if_ac()), dBound, rBound);
  }

  @Override
  public Set<String> violationRemedySuggestions() {
    return Set.of("""
        Find the group that studies on both first and last slot during a day,
        and remove one of the two slots""");
  }
}
