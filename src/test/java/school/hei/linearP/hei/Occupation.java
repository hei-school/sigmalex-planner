package school.hei.linearP.hei;

import school.hei.linearP.hei.costly.AwardedCourse;
import school.hei.linearP.hei.costly.Date;
import school.hei.linearP.hei.costly.Room;
import school.hei.linearP.hei.costly.Slot;

public record Occupation(AwardedCourse ac, Date date, Slot slot, Room r) {
}
