package school.hei.linearP.hei.constraint;

import school.hei.linearE.LinearE;
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
import school.hei.linearP.hei.constraint.sub.no_more_than_one_ac_with_same_c_for_two_consecutive_days;
import school.hei.linearP.hei.constraint.sub.only_one_slot_max_per_course_per_day;
import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Group;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;
import school.hei.linearP.solver.ORTools;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
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
import static school.hei.linearP.hei.constraint.ThreeValuedLogic.true_3vl;
import static school.hei.linearP.hei.constraint.ThreeValuedLogic.unknown_3vl;

public class HEITimetableConstraint implements ViolatorConstraint {

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

  @Override
  public ThreeValuedLogic isViolator() {
    var subViolableConstraints = new HashSet<>(subViolableConstraints());
    subViolableConstraints.removeIf(subC -> subC.getClass().equals(getClass()));
    var subConstraints = and(subViolableConstraints.stream()
        .map(ViolatorConstraint::constraint)
        .toArray(Constraint[]::new));

    var milp = new MILP(min, objective(), base_constraints(), subConstraints);
    var solution = new ORTools().solve(milp);
    return solution.isEmpty() ? unknown_3vl : true_3vl;
  }

  public Set<Violation> detectViolations() {
    if (!solve().isEmpty()) {
      return Set.of();
    }

    return subViolableConstraints().stream()
        .filter(violatorConstraint -> true_3vl.equals(violatorConstraint.isViolator()))
        .map(violatorConstraint -> new Violation(
            violatorConstraint.getClass().getSimpleName(),
            violatorConstraint.violationRemedySuggestions()))
        .collect(toSet());
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
    return new MILP(min, objective(), base_constraints(), constraint());
  }

  public Constraint base_constraints() {
    return and(costs(), domains(), already_provided_occupations());
  }

  private LinearE objective() {
    return prioritize_early_days_and_slots().objective();
  }

  private Constraint costs() {
    return prioritize_early_days_and_slots().constraint();
  }

  private Constraint domains() {
    return pic(and(leq(0, o_ac_d_s_r), leq(o_ac_d_s_r, 1)), acBound, dBound, sBound, rBound);
  }

  private Constraint already_provided_occupations() {
    return and(timetable.getOccupations().stream()
        .map(occupation -> eq(new Z(occupation.toString()), 1))
        .toArray(Constraint[]::new));
  }

  private Set<ViolatorConstraint> subViolableConstraints() {
    return Set.of(
        new exclude_days_off(timetable),
        new only_one_slot_max_per_course_per_day(timetable),
        new no_group_studies_all_day_long(timetable),
        new a_group_can_only_study_a_course_at_a_time(timetable),
        new no_more_than_one_ac_with_same_c_for_two_consecutive_days(timetable),
        new finish_course_hours_with_available_teachers(timetable));
  }

  private Set<Constraint> subConstraints() {
    return subViolableConstraints().stream()
        .map(ViolatorConstraint::constraint)
        .collect(toSet());
  }

  @Override
  public Constraint constraint() {
    return and(subConstraints().toArray(Constraint[]::new));
  }

  protected Instantiator<Group> if_ac() {
    return (Group costly, SubstitutionContext ctx) -> {
      var ctx_ac = (AwardedCourse) (ctx.get(ac).costly());
      return costly.equals(ctx_ac.group()) ? new Constant<>(1) : new Constant<>(0);
    };
  }
}
