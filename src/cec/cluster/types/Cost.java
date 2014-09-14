package cec.cluster.types;

import cec.cluster.Cluster;

/**
 *
 * @author Krzysztof
 */
public abstract class Cost {

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

    protected void setOptions(TypeOptions options) {
        if(options != null || !options.isEmpty())
            throw new RuntimeException("Please configure your cluster kind with the given options\n - write your implemtnation of this method in your class.");
    }
}
