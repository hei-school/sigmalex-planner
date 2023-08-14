package school.hei.planner.constraint;

import java.util.Set;

public record Violation(String constraintName, Set<String> remedySuggestions) {

}
