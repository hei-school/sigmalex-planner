package school.hei.linearP.hei.constraint.sub;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.constraint.HEITimetableConstraint;
import school.hei.linearP.hei.costly.Slot;

import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;
import static school.hei.linearP.hei.costly.Slot.f08t10;
import static school.hei.linearP.hei.costly.Slot.f15t17;

public class no_group_studies_all_day_long extends HEITimetableConstraint {
  public no_group_studies_all_day_long(HEITimetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    var s1s4Bound = new Bound<>(s, new Slot[]{f08t10, f15t17});
    return pic(leq(sigma(o_ac_d_s_r, s1s4Bound), 1), acBound, dBound, rBound);
  }
}
