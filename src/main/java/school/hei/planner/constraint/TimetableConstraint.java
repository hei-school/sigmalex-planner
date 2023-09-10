package school.hei.planner.constraint;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.planner.Occupation;
import school.hei.planner.Timetable;
import school.hei.planner.constraint.sub.constant.a_group_can_only_study_a_course_at_a_time;
import school.hei.planner.constraint.sub.constant.exclude_days_off;
import school.hei.planner.constraint.sub.constant.finish_course_hours_with_available_teachers;
import school.hei.planner.constraint.sub.constant.no_group_studies_all_day_long;
import school.hei.planner.constraint.sub.constant.no_more_than_one_ac_with_same_c_for_two_consecutive_days;
import school.hei.planner.constraint.sub.constant.only_one_slot_max_per_course_per_day;
import school.hei.planner.constraint.sub.exp.a_teacher_cannot_move_location_half_a_day;
import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Group;
import school.hei.planner.costly.Room;
import school.hei.planner.costly.Slot;
import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearE.instantiableE.B;
import school.hei.sigmalex.linearE.instantiableE.Bound;
import school.hei.sigmalex.linearE.instantiableE.BounderQ;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.Instantiator;
import school.hei.sigmalex.linearE.instantiableE.Q;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearP.MILP;
import school.hei.sigmalex.linearP.constraint.Constraint;
import school.hei.sigmalex.linearP.solver.ORTools;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.planner.Occupation.courseNameFromOccupation;
import static school.hei.planner.Occupation.dateNameFromOccupation;
import static school.hei.planner.Occupation.groupNameFromOccupation;
import static school.hei.planner.Occupation.roomNameFromOccupation;
import static school.hei.planner.Occupation.slotNameFromOccupation;
import static school.hei.planner.Occupation.teacherNameFromOccupation;
import static school.hei.planner.constraint.ThreeValuedLogic.true_3vl;
import static school.hei.planner.constraint.ThreeValuedLogic.unknown_3vl;
import static school.hei.sigmalex.linearE.LEFactory.mult;
import static school.hei.sigmalex.linearE.LEFactory.sigma;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.addie;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.multie;
import static school.hei.sigmalex.linearP.OptimizationType.min;
import static school.hei.sigmalex.linearP.constraint.Constraint.and;
import static school.hei.sigmalex.linearP.constraint.Constraint.eq;
import static school.hei.sigmalex.linearP.constraint.Constraint.forall;
import static school.hei.sigmalex.linearP.constraint.Constraint.geq;

@Slf4j
public class TimetableConstraint implements ViolatorConstraint {

  private static final String OCCUPATION_VAR_MAIN_NAME = "occupation";
  @Getter
  protected final Timetable timetable;
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
  protected final B o_ac_d_s_r;
  protected final Q cost_ac_d_s_r = new Q("cost", ac, d, s, r);

  private final boolean withExpConstraints;

  public TimetableConstraint(Timetable timetable, boolean withExpConstraints) {
    this.timetable = timetable;
    this.acBound = new Bound<>(ac, timetable.getAwardedCourses());
    this.gBound = new Bound<>(g, timetable.groups().toArray(Group[]::new));
    this.dBound = new Bound<>(d, timetable.getDatesAll());
    this.doffBound = new Bound<>(d, timetable.getDatesOff());
    this.sBound = new Bound<>(s, timetable.getSlots());
    this.rBound = new Bound<>(r, timetable.getRooms());
    this.o_ac_d_s_r = new B(OCCUPATION_VAR_MAIN_NAME, ac, d, s, r);
    this.withExpConstraints = withExpConstraints;
  }

  public TimetableConstraint(Timetable timetable) {
    this(timetable, false);
  }

  public static Occupation occupationFrom(String occupationString, Timetable timetable) {
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

  protected MILPContext prioritize_early_days_and_slots() {
    var cost = sigma(cost_ac_d_s_r, acBound, dBound, sBound, rBound);
    var cost_d_s_unlinked_to_o = addie(multie(d, Slot.SLOTS_IN_A_DAY), s);
    var assign_costs =
        forall(eq(cost_ac_d_s_r, mult(cost_d_s_unlinked_to_o, o_ac_d_s_r)),
            acBound, dBound.wiq(Date::cost), sBound.wiq(Slot::cost), rBound);
    return new MILPContext(cost, assign_costs);
  }

  public Set<Occupation> solve() {
    var solution = new ORTools().solve(milp());

    Set<Occupation> res = new HashSet<>();
    var solution_byOccupationString =
        solution.optimalNonNullVariablesForUnboundedName(OCCUPATION_VAR_MAIN_NAME);
    solution_byOccupationString.forEach((occupationString, value) -> {
      if (value != 1) {
        throw new RuntimeException(String.format(
            "Value expected to equal 1, but was: %.2f for %s", value, occupationString));
      }
      res.add(occupationFrom(occupationString, timetable));
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
    return forall(geq(cost_ac_d_s_r, 0), acBound, dBound, sBound, rBound);
  }

  private Constraint already_provided_occupations() {
    return and(timetable.getOccupations().stream()
        .map(occupation -> eq(new B(occupation.toString()), 1))
        .toArray(Constraint[]::new));
  }

  private Set<ViolatorConstraint> constConstraints() {
    return Set.of(
        new exclude_days_off(timetable, withExpConstraints),
        new only_one_slot_max_per_course_per_day(timetable, withExpConstraints),
        new no_group_studies_all_day_long(timetable, withExpConstraints),
        new a_group_can_only_study_a_course_at_a_time(timetable, withExpConstraints),
        new no_more_than_one_ac_with_same_c_for_two_consecutive_days(timetable, withExpConstraints),
        new finish_course_hours_with_available_teachers(timetable, withExpConstraints));
  }

  private Set<ViolatorConstraint> expConstraints() {
    return Set.of(
        new a_teacher_cannot_move_location_half_a_day(timetable, withExpConstraints));
  }

  private Set<Constraint> subConstraints() {
    var constConstraint = and(constConstraints().stream()
        .map(ViolatorConstraint::constraint)
        .collect(toSet()));

    if (withExpConstraints) {
      var expConstraint = and(expConstraints().stream()
          .map(ViolatorConstraint::constraint)
          .collect(toSet()));
      return Set.of(constConstraint, expConstraint);
    }

    return Set.of(constConstraint);
  }

  private Set<ViolatorConstraint> subViolableConstraints() {
    var constConstraints = constConstraints();
    if (withExpConstraints) {
      var res = new HashSet<>(constConstraints);
      res.addAll(expConstraints());
      return res;
    }

    return constConstraints;
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
