package cec.cluster.types;

import cec.cluster.types.gaussian.Gaussian;

/**
 *
 * @author Krzysztof
 */
public enum ClusterKind {

    Gaussian("Gaussian", false);
//    GaussianCov("GaussianCov"),
//    GaussianR("GaussianR"),
//    GaussianRScale("GaussianRScale"),
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
            case Gaussian:
                return new Gaussian();
//            case GaussianCov:
//                return new GaussianCov();
//            case GaussianR:
//                return new GaussianR();
//            case GaussianRScale:
//                return new GaussianRScale();
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
