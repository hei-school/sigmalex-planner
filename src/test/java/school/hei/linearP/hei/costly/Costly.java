package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public sealed interface Costly<T> extends BounderValue<T>
    permits AwardedCourse, Course, Date, Group, Room, Slot, Teacher {
}
