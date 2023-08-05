package school.hei.linearP.hei.costly;

import school.hei.linearE.instantiableE.BounderValue;

public class Costly implements BounderValue<Costly> {
  @Override
  public Costly costly() {
    return this;
  }
}
