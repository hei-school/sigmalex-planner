package school.hei.planner.costly;

import lombok.Value;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static java.time.Month.JANUARY;
import static java.time.temporal.ChronoUnit.DAYS;

@Value
public class Date implements Costly<Date> {

  private static final LocalDate DEFAULT_REFERENCE_DATE = LocalDate.of(2023, JANUARY, 1);
  private static final double DEFAULT_COST_WEIGHT = 1_000;
  LocalDate localDate;

  public Date(int year, Month month, int day) {
    this.localDate = LocalDate.of(year, month, day);
  }

  private Date(LocalDate localDate) {
    this.localDate = localDate;
  }

  public boolean isConsecutivelyAfter(Date that) {
    return ChronoUnit.DAYS.between(that.localDate, localDate) == 1;
  }

  public Date next() {
    return new Date(localDate.plusDays(1));
  }

  @Override
  public String toString() {
    return (localDate.getMonth().toString().substring(0, 3) + localDate.getDayOfMonth()).toLowerCase();
  }

  @Override
  public Date costly() {
    return this;
  }

  public double cost() {
    return DEFAULT_COST_WEIGHT * DAYS.between(DEFAULT_REFERENCE_DATE, localDate);
  }
}
