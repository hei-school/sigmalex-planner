package school.hei.planner.constraint.sub;

import school.hei.linearE.LinearE;
import school.hei.linearE.instantiableE.B;
import school.hei.linearP.constraint.Constraint;
import school.hei.planner.HEITimetable;
import school.hei.planner.Occupation;
import school.hei.planner.constraint.HEITimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Slot;

import static school.hei.linearE.LEFactory.add;
import static school.hei.linearE.LEFactory.mono;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.True.TRUE;

public class no_more_than_one_ac_with_same_c_for_two_consecutive_days extends HEITimetableConstraint {
  public no_more_than_one_ac_with_same_c_for_two_consecutive_days(HEITimetable timetable) {
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
