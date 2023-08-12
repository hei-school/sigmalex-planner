package school.hei.linearP.hei.constraint;

import school.hei.linearE.instantiableE.Bound;
import school.hei.linearE.instantiableE.BounderQ;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.Instantiator;
import school.hei.linearE.instantiableE.Q;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Z;
import school.hei.linearP.MILP;
import school.hei.linearP.constraint.Constraint;
import school.hei.linearP.hei.HEITimetable;
import school.hei.linearP.hei.Occupation;
import school.hei.linearP.hei.constraint.sub.a_group_can_only_study_a_course_at_a_time;
import school.hei.linearP.hei.constraint.sub.exclude_days_off;
import school.hei.linearP.hei.constraint.sub.finish_course_hours_with_available_teachers;
import school.hei.linearP.hei.constraint.sub.no_group_studies_all_day_long;
import school.hei.linearP.hei.constraint.sub.only_one_slot_max_per_course_per_day;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.solver.ORTools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static school.hei.linearE.LEFactory.mult;
import static school.hei.linearE.LEFactory.sigma;
import static school.hei.linearE.instantiableE.IEFactory.addie;
import static school.hei.linearE.instantiableE.IEFactory.multie;
import static school.hei.linearP.OptimizationType.min;
import static school.hei.linearP.constraint.Constraint.and;
import static school.hei.linearP.constraint.Constraint.eq;
import static school.hei.linearP.constraint.Constraint.geq;
import static school.hei.linearP.constraint.Constraint.leq;
import static school.hei.linearP.constraint.Constraint.pic;
import static school.hei.linearP.hei.Occupation.courseNameFromOccupation;
import static school.hei.linearP.hei.Occupation.dateNameFromOccupation;
import static school.hei.linearP.hei.Occupation.groupNameFromOccupation;
import static school.hei.linearP.hei.Occupation.roomNameFromOccupation;
import static school.hei.linearP.hei.Occupation.slotNameFromOccupation;
import static school.hei.linearP.hei.Occupation.teacherNameFromOccupation;

public class HEITimetableConstraint implements SATConstraint {

  private static final String OCCUPATION_VAR_MAIN_NAME = "occupation";
  protected final HEITimetable timetable;
  protected final BounderQ<AwardedCourse> ac = new BounderQ<>("ac");
  protected final BounderQ<Group> g = new BounderQ<>("g");
  protected final BounderQ<Date> d = new BounderQ<>("d");
  protected final BounderQ<Slot> s = new BounderQ<>("s");
  protected final BounderQ<Room> r = new BounderQ<>("r");
  protected final Bound<AwardedCourse> acBound;
  protected final Bound<Group> gBound;
  protected final Bound<Date> dBound;
  protected final Bound<Date> doffBound;
  protected final Bound<Slot> sBound;
  protected final Bound<Room> rBound;
  protected final Z o_ac_d_s_r;

  public HEITimetableConstraint(HEITimetable timetable) {
    this.timetable = timetable;
    this.acBound = new Bound<>(ac, timetable.getAwardedCourses());
    this.gBound = new Bound<>(g, timetable.groups().toArray(Group[]::new));
    this.dBound = new Bound<>(d, timetable.getDatesAll());
    this.doffBound = new Bound<>(d, timetable.getDatesOff());
    this.sBound = new Bound<>(s, timetable.getSlots());
    this.rBound = new Bound<>(r, timetable.getRooms());
    this.o_ac_d_s_r = new Z(OCCUPATION_VAR_MAIN_NAME, ac, d, s, r);
  }

  protected MILPContext prioritize_early_days_and_slots() {
    var cost_d_s = new Q("cost", d, s);
    var cost_ac_d_s_r = new Q("cost", ac, d, s, r);
    var cost_domains = and(
        pic(geq(cost_d_s, 0), dBound, sBound),
        pic(geq(cost_ac_d_s_r, 0), dBound, sBound, rBound));

    var cost = sigma(cost_ac_d_s_r, acBound, dBound, sBound, rBound);
    var cost_d_s_unlinked_to_o = addie(multie(d, Slot.SLOTS_IN_A_DAY), s);
    var assign_costs =
        pic(eq(cost_ac_d_s_r, mult(cost_d_s_unlinked_to_o, o_ac_d_s_r)),
            acBound, dBound.wiq(Date::cost), sBound.wiq(Slot::cost), rBound);
    return new MILPContext(cost, and(cost_domains, assign_costs));
  }

  public Set<Occupation> solve() {
    var solution = new ORTools().solve(milp());

    Set<Occupation> res = new HashSet<>();
    var solution_byOccupationString =
        solution.optimalBoundedVariablesForUnboundedName(OCCUPATION_VAR_MAIN_NAME);
    solution_byOccupationString.forEach((occupationString, value) -> {
      if (value != 1) {
        throw new RuntimeException(String.format(
            "Value expected to equal 1, but was: %.2f for %s", value, occupationString));
      }
      res.add(occupationFrom(occupationString));
    });

    return res;
  }

  private Occupation occupationFrom(String occupationString) {
    var courseName = courseNameFromOccupation(occupationString);
    var groupName = groupNameFromOccupation(occupationString);
    var teacherName = teacherNameFromOccupation(occupationString);
    var dateName = dateNameFromOccupation(occupationString);
    var slotName = slotNameFromOccupation(occupationString);
    var roomName = roomNameFromOccupation(occupationString);
    return new Occupation(
        new AwardedCourse(
            timetable.courseByName(courseName),
            timetable.groupByName(groupName),
            timetable.teacherByName(teacherName)),
        timetable.dateByName(dateName),
        timetable.slotByName(slotName),
        timetable.roomByName(roomName));
  }

  private MILP milp() {
    var domains =
        pic(and(leq(0, o_ac_d_s_r), leq(o_ac_d_s_r, 1)), acBound, dBound, sBound, rBound);
    var prioritize_early_days_and_slots_context = prioritize_early_days_and_slots();
    return new MILP(
        min, prioritize_early_days_and_slots_context.objective(),
        prioritize_early_days_and_slots_context.constraint(),
        domains, constraint());
  }

  private List<SATConstraint> subConstraints() {
    return List.of(
        new exclude_days_off(timetable),
        new only_one_slot_max_per_course_per_day(timetable),
        new no_group_studies_all_day_long(timetable),
        new a_group_can_only_study_a_course_at_a_time(timetable),
        new finish_course_hours_with_available_teachers(timetable));
  }

  @Override
  public Constraint constraint() {
    return and(subConstraints().stream()
        .map(SATConstraint::constraint)
        .toArray(Constraint[]::new));
  }

  protected Instantiator<Group> if_ac() {
    return (Group costly, SubstitutionContext ctx) -> {
      var ctx_ac = (AwardedCourse) (ctx.get(ac).costly());
      return costly.equals(ctx_ac.group()) ? new Constant<>(1) : new Constant<>(0);
    };
  }
}
