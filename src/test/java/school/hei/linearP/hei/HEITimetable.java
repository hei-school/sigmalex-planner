package school.hei.linearP.hei;

import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;

public record HEITimetable(
    AwardedCourse[] awarded_courses,
    Room[] rooms,
    Date[] dates_all,
    Date[] dates_off,
    Slot[] slots) {
}
