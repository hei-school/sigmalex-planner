package school.hei.planner;

import school.hei.planner.costly.AwardedCourse;
import school.hei.planner.costly.Date;
import school.hei.planner.costly.Room;
import school.hei.planner.costly.Slot;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

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

  public static String toOrderedLines(Set<Occupation> occupations) {
    return occupations.stream()
        .sorted(Occupation::compareOccupationEntry)
        .map(Occupation::toString)
        .collect(joining("\n"));
  }

  private static int compareOccupationEntry(Occupation o1, Occupation o2) {
    var name1 = o1.toString();
    var name2 = o2.toString();

    var compareDates = dateNameFromOccupation(name1).compareTo(dateNameFromOccupation(name2));
    if (compareDates != 0) {
      return compareDates;
    }

    var compareSlots = slotNameFromOccupation(name1).compareTo(slotNameFromOccupation(name2));
    if (compareSlots != 0) {
      return compareSlots;
    }

    return roomNameFromOccupation(name1).compareTo(roomNameFromOccupation(name2));
  }

  @Override
  public String toString() {
    return String.format("occupation[ac:%s][d:%s][r:%s][s:%s]", awardedCourse, date, room, slot);
  }
}
