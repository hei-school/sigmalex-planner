package school.hei.sigmalex.linearE.instantiableE;

public interface Bounder<Costly> {
  Variable variable();

  Instantiator<Costly> instantiator();

  Bounder<Costly> wi(Instantiator<Costly> instantiator);
}
