package school.hei.planner.costly;

import school.hei.sigmalex.linearE.instantiableE.BounderValue;

public sealed interface Costly<T> extends BounderValue<T>
    permits AwardedCourse, Course, Date, Group, Location, Room, Slot, Teacher {
}
