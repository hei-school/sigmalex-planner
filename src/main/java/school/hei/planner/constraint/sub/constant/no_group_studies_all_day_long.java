package school.hei.planner.constraint.sub.constant;

import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.costly.Slot;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearP.constraint.Constraint;

import java.util.Set;

import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;
import static school.hei.sigmalex.linearP.constraint.Constraint.pic;

public class no_group_studies_all_day_long extends TimetableConstraint {
  public no_group_studies_all_day_long(Timetable timetable, boolean withExpConstraints) {
    super(timetable, withExpConstraints);
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
