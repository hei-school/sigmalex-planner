package school.hei.linearP.hei.constraint;

import java.util.Set;

public record Violation(String constraintName, Set<String> remedySuggestions) {

}
