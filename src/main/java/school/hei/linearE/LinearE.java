package school.hei.linearE;

public sealed interface LinearE
    permits Mono, Add, VariadicAdd, Sigma, VariadicSigma, Mult, Normalized {
  Normalized normalize();
}
