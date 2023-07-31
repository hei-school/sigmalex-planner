package school.hei.linearP.hei.costly;

import java.time.LocalDate;
import java.time.Month;

import static java.time.Month.JANUARY;
import static java.time.temporal.ChronoUnit.DAYS;

public class Date extends CostlyBounderValue {

  private static final LocalDate DEFAULT_REFERENCE_DATE = LocalDate.of(2023, JANUARY, 1);
  private static final double DEFAULT_COST_WEIGHT = 1_000;
  private final LocalDate localDate;

  public Date(int year, Month month, int day) {
    this.localDate = LocalDate.of(year, month, day);
  }

  @Override
  public double cost() {
    return DEFAULT_COST_WEIGHT * DAYS.between(DEFAULT_REFERENCE_DATE, localDate);
  }

  @Override
  public String toString() {
    return (localDate.getMonth().toString().substring(0, 3) + localDate.getDayOfMonth()).toLowerCase();
  }
}
