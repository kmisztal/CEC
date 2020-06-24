package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import cec.cluster.types.TypeOptions;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class DeterminantGaussians extends Cost {

    private static final String PARAM = "det";
    private double det;

    @Override
    public double h() {
        return cluster.getWeight()
                * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.pow(cluster.getCov().determinant() / det, 1. / cluster.getDimension())
                + 0.5 * Math.log(det));
    }

    @Override
    public String getInfo() {
        return "Gaussians with a covaraince matrix with given determinant";
    }

    @Override
    public Cost setOptions(TypeOptions options) {
        checkConfiguration(options);
        det = (Double) options.get(PARAM);
        return this;
    }

    @Override
    public SimpleMatrix getCov() {
        final double v = cluster.getCov().determinant();
        if (v == 0) {
            return cluster.getCov();
        } else {
            return cluster.getCov().scale(det / v);
        }
    }

    @Override
    public int getModelComplexity() {
        final int n = cluster.getDimension();
        return n * (n + 1) / 2 - 1 //cov (because determinat fix the cov)
                + n; //mean
    }
}
