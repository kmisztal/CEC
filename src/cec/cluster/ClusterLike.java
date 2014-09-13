package cec.cluster;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public interface ClusterLike {

    public double getWeight();

    public SimpleMatrix getMean();

    public SimpleMatrix getCov();

    public int getCardinality();
    
}
