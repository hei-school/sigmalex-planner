package school.hei.le;

public sealed interface LinearExpression
    permits Mono, Add, VariadicAdd, Sigma, VariadicSigma, Mult, Normalized {
  Normalized normalize();
}
