package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class DiagonalGaussians extends Cost {

    @Override
    public double h() {
        double det = 1;
        for (int i = 0; i < cluster.getCov().numRows(); ++i) {
            det *= cluster.getCov().get(i, i);
        }
        return cluster.getWeight() * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2. * Math.PI * Math.E)
                + 0.5 * Math.log(det));
    }

    @Override
    public String getInfo() {
        return "Diagonal Gaussians: Gaussians with diagonal covaraince";
    }

    @Override
    public SimpleMatrix getCov() {
        final int N = cluster.getDimension();
        double diag[] = new double[N];
        for (int i = 0; i < N; ++i) {
            diag[i] = this.cluster.getCov().get(i, i);
        }
        return SimpleMatrix.diag(diag);
    }

}
