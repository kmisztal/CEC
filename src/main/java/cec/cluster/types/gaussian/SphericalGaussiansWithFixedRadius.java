package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import cec.cluster.types.TypeOptions;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class SphericalGaussiansWithFixedRadius extends Cost {

    private static final String PARAM = "r";
    private double r;

    @Override
    public double h() {
        return cluster.getWeight()
                * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2 * Math.PI)
                + 0.5 * cluster.getCov().trace() / r
                + 0.5 * Math.log(r));
    }

    @Override
    public String getInfo() {
        return "Spherical Gaussians with a fixed radius: radial Gaussian densities";
    }

    @Override
    public Cost setOptions(TypeOptions options) {
        checkConfiguration(options);
        r = (Double) options.get(PARAM);
        return this;
    }
    
    @Override
    public SimpleMatrix getCov() {
        return SimpleMatrix.identity(this.cluster.getCov().numCols()).scale(r);
    }

    public int getModelComplexity() {
        final int n = cluster.getDimension();
        return 1 //cov (we just remember fixed radius)
                + n; //mean
    }
}
