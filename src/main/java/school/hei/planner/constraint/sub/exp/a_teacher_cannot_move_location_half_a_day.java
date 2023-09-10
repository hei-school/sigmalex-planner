package school.hei.planner.constraint.sub.exp;

import lombok.extern.slf4j.Slf4j;
import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Slot;
import school.hei.planner.costly.Teacher;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Instantiator;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.constraint.Constraint;

import java.util.AbstractMap;
import java.util.Set;

import static school.hei.planner.costly.Slot.afternoon;
import static school.hei.planner.costly.Slot.morning;
import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearE.instantiableE.Bound.bound;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.forall;
import static school.hei.sigmalex.linearP.constraint.Constraint.or;

@Slf4j
public class a_teacher_cannot_move_location_half_a_day extends TimetableConstraint {
  public a_teacher_cannot_move_location_half_a_day(Timetable timetable, boolean withExpConstraints) {
    super(timetable, withExpConstraints);
  }

  @Override
  public Constraint constraint() {
    return and(stay_at_location(morning()), stay_at_location(afternoon()));
  }

  private Constraint stay_at_location(Set<Slot> slots) {
    var locationsPair = locationsPair();
    var rLocation0Bound = locationsPair.getKey();
    var rLocation1Bound = locationsPair.getValue();

    var sBound = bound(s, slots);

    var t = new BounderQ<Teacher>("t");
    var tBound = bound(t, timetable.teachers());
    Instantiator<Teacher> tInstantiator = (Teacher costly, SubstitutionContext ctx) -> {
      var ctx_ac = (AwardedCourse) (ctx.get(ac).costly());
      return costly.equals(ctx_ac.teacher()) ? new Constant<>(1) : new Constant<>(0);
    };

    return forall(or(
            eq(sigma(mult(t, o_ac_d_s_r), acBound, dBound, sBound, rLocation0Bound), 0),
            eq(sigma(mult(t, o_ac_d_s_r), acBound, dBound, sBound, rLocation1Bound), 0)),
        tBound.wi(tInstantiator));
  }

  private AbstractMap.SimpleImmutableEntry<Bound<?>, Bound<?>> locationsPair() {
    if (timetable.getLocations().length != 2) {
      throw new RuntimeException("TODO");
    }

    var rooms_location0 = timetable.getLocations()[0].rooms();
    var rooms_location1 = timetable.getLocations()[1].rooms();
    var rBound0 = bound(r, rooms_location0);
    var rBound1 = bound(r, rooms_location1);
    return new AbstractMap.SimpleImmutableEntry<>(rBound0, rBound1);
  }
}
