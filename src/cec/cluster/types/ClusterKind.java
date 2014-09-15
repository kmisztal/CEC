package cec.cluster.types;

import cec.cluster.types.gaussian.Gaussians;
import cec.cluster.types.gaussian.SphericalGaussians;

/**
 *
 * @author Krzysztof
 */
public enum ClusterKind {

    Gaussians("Gaussians", false),
//    GaussianCov("GaussianCov"),
//    GaussianR("GaussianR"),
    SphericalGaussians("SphericalGaussians", false);
//    GaussianDet("GaussianDet"),
//    GaussianDiag("GaussianDiag"),
//    GaussianLambda("GaussianLambda");
    
    /**
     * name of cluster type
     */
    private final String name;
    /**
     * if additional configuration is needed then type parameter is true
     */
    private final boolean optionNeeded;

    private ClusterKind(String name, boolean optionNeeded) {
        this.name = name;
        this.optionNeeded = optionNeeded;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isOptionNeeded() {
        return optionNeeded;
    }

    public Cost getFunction() {
        switch (this) {
            case Gaussians:
                return new Gaussians();
//            case GaussianCov:
//                return new GaussianCov();
//            case GaussianR:
//                return new GaussianR();
            case SphericalGaussians:
                return new SphericalGaussians();
//            case GaussianDet:
//                return new GaussianDet();
//            case GaussianDiag:
//                return new GaussianDiag();
//            case GaussianLambda:
//                return new GaussianLambda();
            default:
                throw new RuntimeException("Bad cluster kind");
        }
    }
}
