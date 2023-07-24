package school.hei.linearE;

public sealed interface LinearE
    permits Add, Mono, Mult, NormalizedLE, Sigma, Sub, VariadicAdd, VariadicSigma {
  NormalizedLE normalize();
}
