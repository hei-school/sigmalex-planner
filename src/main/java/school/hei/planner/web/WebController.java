package school.hei.planner.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.planner.SolvedTimetable;
import school.hei.planner.Timetable;
import school.hei.planner.constraint.TimetableConstraint;

import java.util.Set;

@RestController
@Api(tags = {"Planner API"}, description = " ")
public class WebController {

  @PostMapping(value = "/timetable", consumes = "application/json", produces = "application/json")
  public SolvedTimetable solveTimetable(@RequestBody Timetable timetable) {
    var timetableConstraint = new TimetableConstraint(timetable);
    var solvedOccupations = timetableConstraint.solve();
    if (!solvedOccupations.isEmpty()) {
      return new SolvedTimetable(timetable.withOccupations(solvedOccupations), Set.of());
    }

    return new SolvedTimetable(timetable, timetableConstraint.detectViolations());
  }
}
