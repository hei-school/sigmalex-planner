package school.hei.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.planner.constraint.Violation;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedTimetable {
  private Timetable timetable;
  private Set<Violation> violations;
}
