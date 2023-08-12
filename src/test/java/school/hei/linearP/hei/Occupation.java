package school.hei.linearP.hei;

import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Occupation(AwardedCourse awardedCourse, Date date, Slot slot, Room room) {

  public static final Pattern OCCUPATION_VAR_PATTERN = Pattern.compile(
      "occupation\\[ac:\\[c:(.*)]\\[g:(.*)]\\[t:(.*)]]\\[d:(.*)]\\[r:(.*)]\\[s:(.*)]");

  public static String courseNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(1);
  }

  public static String groupNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(2);
  }

  public static String teacherNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(3);
  }

  public static String dateNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(4);
  }

  public static String roomNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(5);
  }

  public static String slotNameFromOccupation(String occupationVarName) {
    return checkedMatcher(OCCUPATION_VAR_PATTERN.matcher(occupationVarName)).group(6);
  }

  private static Matcher checkedMatcher(Matcher uncheckedMatcher) {
    if (!uncheckedMatcher.find()) {
      throw new RuntimeException("Pattern does not match");
    }
    return uncheckedMatcher;
  }

  @Override
  public String toString() {
    return String.format("occupation[ac:%s][d:%s][r:%s][s:%s]", awardedCourse, date, room, slot);
  }
}
