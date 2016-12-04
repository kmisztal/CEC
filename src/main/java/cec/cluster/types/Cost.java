package cec.cluster.types;

import cec.cluster.Cluster;
import cec.cluster.Point;
import org.ejml.simple.SimpleMatrix;

import static java.lang.Math.*;

/**
 * @author Krzysztof
 */
public abstract class Cost {
    protected static final boolean needConfiguration = false;
    protected Cluster cluster;

    public Cost() {
        this.cluster = null;
    }

    public void setCluster(Cluster c) {
        this.cluster = c;
    }

    public abstract double h();

    @Override
    public String toString() {
        return getInfo();
    }

    public abstract String getInfo();

    public Cost setOptions(TypeOptions options) {
        return this;
    }

    protected void checkConfiguration(TypeOptions options) {
        if (needConfiguration && (options == null || options.isEmpty()))
            throw new RuntimeException("Please configure your cluster kind with the given options\n - write your implementation of this method in your class.");
    }

    /**
     * @return covariance matrix for current cost model
     */
    public abstract SimpleMatrix getCov();

    public double getValueAt(final SimpleMatrix mean, final Point p) {
        //TODO: make same optimalization
        final SimpleMatrix dist = mean.minus(p.getMean());
        return exp(-0.5 * dist.transpose().mult(getCov().invert()).mult(dist).get(0, 0)) / (pow(2. * PI, p.getDimension() / 2.) * sqrt(getCov().determinant()));
    }

    /**
     * number of free parameters to calculate the model
     *
     * @return
     */
    public abstract int numberOfFreeParameters();
}
