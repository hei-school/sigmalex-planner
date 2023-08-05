package school.hei.linearE.instantiableE;

public interface Bounder<Costly> {
  Variable<Costly> variable();

  Instantiator<Costly> instantiator();

  Bounder<Costly> wi(Instantiator<Costly> instantiator);
}
