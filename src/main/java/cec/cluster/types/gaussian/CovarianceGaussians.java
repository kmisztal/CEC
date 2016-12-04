package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import cec.cluster.types.TypeOptions;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class CovarianceGaussians extends Cost {

    private static final String PARAM = "covariance";
    private SimpleMatrix sigma;
    private SimpleMatrix sigma_inv;
    private double sigma_det;

    @Override
    public double h() {
        return cluster.getWeight()
                * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2. * Math.PI)
                + 0.5 * (sigma_inv.mult(cluster.getCov())).trace()
                + 0.5 * Math.log(sigma_det)
                );
    }

    @Override
    public String getInfo() {
        return "Gaussians with specified covariance matrix";
    }

    @Override
    public Cost setOptions(TypeOptions options) {
        checkConfiguration(options);
        sigma = new SimpleMatrix((double[][]) options.get(PARAM));
        sigma_inv = sigma.invert();
        sigma_det = 1./sigma_inv.determinant();
        return this;
    }
    
    @Override
    public SimpleMatrix getCov() {
        return sigma;
    }

    @Override
    public int numberOfFreeParameters() {
        return cluster.getDimension();
    }
}