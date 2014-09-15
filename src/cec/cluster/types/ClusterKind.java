package cec.cluster.types;

import cec.cluster.types.gaussian.CovarianceGaussians;
import cec.cluster.types.gaussian.DeterminantGaussians;
import cec.cluster.types.gaussian.DiagonalGaussians;
import cec.cluster.types.gaussian.Gaussians;
import cec.cluster.types.gaussian.LambdaGaussians;
import cec.cluster.types.gaussian.SphericalGaussians;
import cec.cluster.types.gaussian.SphericalGaussiansWithFixedRadius;

/**
 *
 * @author Krzysztof
 */
public enum ClusterKind {

    Gaussians("Gaussians", false),
    CovarianceGaussians("CovarianceGaussians", true),
    SphericalGaussiansWithFixedRadius("SphericalGaussiansWithFixedRadius", true),
    SphericalGaussians("SphericalGaussians", false),
    DeterminantGaussians("DeterminantGaussians", true),
    DiagonalGaussians("DiagonalGaussians", false),
    LambdaGaussians("LambdaGaussians", true);
    
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
            case CovarianceGaussians:
                return new CovarianceGaussians();
            case SphericalGaussiansWithFixedRadius:
                return new SphericalGaussiansWithFixedRadius();
            case SphericalGaussians:
                return new SphericalGaussians();
            case DeterminantGaussians:
                return new DeterminantGaussians();
            case DiagonalGaussians:
                return new DiagonalGaussians();
            case LambdaGaussians:
                return new LambdaGaussians();
            default:
                throw new RuntimeException("Bad cluster kind");
        }
    }
}
