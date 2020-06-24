package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;
import cec.cluster.types.TypeOptions;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class LambdaGaussians extends Cost {

    private static final String PARAM = "lambda";
    private double [] lambda;

    @Override
    public double h() {
        SimpleEVD eig = cluster.getCov().eig();
        double r1 = 0;
        double r2 = 0;
        for(int i = 0; i < lambda.length; ++i){
            r1 += eig.getEigenvalue(i).getReal() / lambda[i];
            r2 += Math.log(lambda[i]);
        }
        return cluster.getWeight()
                * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2 * Math.PI)
                + 0.5 * r1
                + 0.5 * r2
                );
    }

    @Override
    public String getInfo() {
        return "Gaussians with specified eigenvalues of covariance matrix";
    }

    @Override
    public Cost setOptions(TypeOptions options) {
        checkConfiguration(options);
        lambda = (double []) options.get(PARAM);
//        if(lambda.length != cluster.getDimension())
//            throw new RuntimeException("The number of given eigenvalues is not correct (" + lambda.length + "!=" + cluster.getDimension());
        return this;
    }
    
    @Override
    public SimpleMatrix getCov() {
        final int N = cluster.getDimension();
        SimpleEVD eig = cluster.getCov().eig();
        SimpleMatrix v = new SimpleMatrix(N, N);
        SimpleMatrix l = SimpleMatrix.diag(lambda);
        for(int i = 0; i < N; ++i){
            SimpleMatrix p = eig.getEigenVector(i);
            for(int j = 0; j < N; ++j){
                v.set(i, j, p.get(j, 0));
            }
        }
        return v.mult(l).mult(v.transpose());
    }

    @Override
    public int getModelComplexity() {
        final int n = cluster.getDimension();
        return n * (n + 1) / 2 //cov
                + n; //mean
    }
}

