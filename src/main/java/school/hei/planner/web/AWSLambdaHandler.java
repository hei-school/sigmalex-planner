package school.hei.planner.web;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import school.hei.planner.Timetable;

import java.util.Map;

import static school.hei.planner.SolvedTimetable.solve;

@Slf4j
public class AWSLambdaHandler implements RequestHandler<Map<String, Object>, String> {

  private static final ObjectMapper om = new ObjectMapper();

  @Override
  public String handleRequest(Map<String, Object> input, Context context) {
    //TODO: too expeditious:
    //  - input: check method, accept and authorization headers
    //  - output: appropriate status code depending on errors
    var timetable = om.readValue(input.get("body").toString(), Timetable.class);
    var solvedTimetable = solve(timetable);
    return om.writeValueAsString(solvedTimetable);
  }
}