package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class SphericalGaussians extends Cost {

    @Override
    public double h() {
        return cluster.getWeight() * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2 * Math.PI * Math.E / cluster.getDimension())
                + 0.5 * cluster.getCov().trace()
                );
    }

    @Override
    public String getInfo() {
        return "Spherical Gaussians: radial Gaussian densities";
    }

    @Override
    public SimpleMatrix getCov() {
        final SimpleMatrix cov = this.cluster.getCov();
        final int N = cov.numRows();
        final double v = cov.trace()/N;
        SimpleMatrix ret = new SimpleMatrix(N, N);
        for(int i = 0; i < N; ++i)
            ret.set(i, i, v);
        return ret;
    }

}
