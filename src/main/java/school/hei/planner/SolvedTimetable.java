package school.hei.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.planner.constraint.TimetableConstraint;
import school.hei.planner.constraint.Violation;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedTimetable {
  private Timetable timetable;
  private Set<Violation> violations;

  public static SolvedTimetable solve(Timetable timetable) {
    var timetableConstraint = new TimetableConstraint(timetable);
    var solvedOccupations = timetableConstraint.solve();
    if (!solvedOccupations.isEmpty()) {
      return new SolvedTimetable(timetable.withOccupations(solvedOccupations), Set.of());
    }

    return new SolvedTimetable(timetable, timetableConstraint.detectViolations());
  }
}
