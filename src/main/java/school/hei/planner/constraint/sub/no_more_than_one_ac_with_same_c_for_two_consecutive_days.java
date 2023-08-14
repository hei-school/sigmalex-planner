package school.hei.planner.constraint.sub;

import school.hei.planner.Occupation;
import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Slot;
import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.B;
import school.hei.sigmalex.linearP.constraint.Constraint;

import static school.hei.sigmalex.linearE.LEFactory.add;
import static school.hei.sigmalex.linearE.LEFactory.mono;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.leq;
import static school.hei.sigmalex.linearP.constraint.True.TRUE;

public class no_more_than_one_ac_with_same_c_for_two_consecutive_days extends TimetableConstraint {
  public no_more_than_one_ac_with_same_c_for_two_consecutive_days(Timetable timetable) {
    super(timetable);
  }

  @Override
  public Constraint constraint() {
    Constraint res = TRUE;
    for (var ac : timetable.getAwardedCourses()) {
      for (var d : timetable.getDatesAll()) {
        res = and(res, leq(add(sum_o_for(d, ac), sum_o_for(d.next(), ac)), 1));
      }
    }
    return res;
  }

  private LinearE sum_o_for(Date d, AwardedCourse ac) {
    LinearE res = mono(0);
    for (var s : Slot.values()) {
      for (var r : timetable.getRooms()) {
        res = add(res, new B(new Occupation(ac, d, s, r).toString()));
      }
    }
    return res;
  }
}
