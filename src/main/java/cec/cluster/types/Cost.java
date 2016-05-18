package cec.cluster.types;

import cec.cluster.Cluster;
import org.ejml.simple.SimpleMatrix;

/**
 *
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
    
    protected void checkCongiguration(TypeOptions options){
        if(needConfiguration && (options == null || options.isEmpty()))
            throw new RuntimeException("Please configure your cluster kind with the given options\n - write your implementation of this method in your class.");
    }
    
    /**
     * 
     * @return covariance matrix for current cost model
     */
    public abstract SimpleMatrix getCov();
}
