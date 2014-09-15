package cec.cluster;

import cec.cluster.types.Cost;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class Cluster implements ClusterLike {

    /**
     * mean of cluster
     */
    private SimpleMatrix mean;

    /**
     * weight
     */
    private double weight;

    /**
     * covarianve matrix
     */
    private SimpleMatrix cov;

    /**
     * cardinality of a cluster
     */
    private int cardinality;

    /**
     * Cost function
     */
    private Cost costFunction;

    private int id;

    private List<ClusterLike> data;

    public Cluster() {
        this.weight = 0.;
        this.cardinality = 0;
        this.mean = null;
        this.cov = null;
        this.costFunction = null;
        this.data = new CopyOnWriteArrayList<>();
    }

    public Cluster(Cost f) {
        this.weight = 0.;
        this.cardinality = 0;
        this.mean = null;
        this.cov = null;
        this.costFunction = f;
        this.data = new CopyOnWriteArrayList<>();
    }

    public Cluster(Cost f, int dim) {
        this.weight = 0.;
        this.cardinality = 0;
        this.mean = new SimpleMatrix(dim, 1);
        this.cov = new SimpleMatrix(dim, dim);
        this.costFunction = f;
        this.costFunction.setCluster(this);
        this.data = new CopyOnWriteArrayList<>();
    }

    public Cluster(SimpleMatrix mean, double weight, SimpleMatrix cov, int cardinality) {
        this.mean = mean;
        this.weight = weight;
        this.cov = cov;
        this.cardinality = cardinality;
        this.data = new CopyOnWriteArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    @Override
    public SimpleMatrix getCov() {
        return cov;
    }

    public void setCovariance(SimpleMatrix cov) {
        this.cov = cov;
    }

    @Override
    public SimpleMatrix getMean() {
        return mean;
    }

    public void setMean(SimpleMatrix mean) {
        this.mean = mean;
    }

    public Cost getCostFunction() {
        return costFunction;
    }

    /**
     *
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public static Cluster add(Cluster A, Cluster B) {//A+B
        Cluster ret = new Cluster();
        ret.setWeight(A.getWeight() + B.getWeight());
        final double pA = A.getWeight() / ret.getWeight();
        final double pB = B.getWeight() / ret.getWeight();

        ret.setMean(A.getMean().scale(pA).plus(B.getMean().scale(pB)));
        ret.setCovariance(A.getCov().scale(pA).plus(B.getCov().scale(pB)).plus(A.getMean().minus(B.getMean()).scale(pA * pB).mult(A.getMean().minus(B.getMean()).transpose())));

        ret.setCardinality(A.getCardinality() + B.getCardinality());

        return ret;
    }

    /**
     * if add is false we make fake adding
     *
     * @param B
     * @param add
     * @return
     */
    public Cluster add(ClusterLike B, boolean add) {//THIS + B
        final double weight_of_sum = this.getWeight() + B.getWeight();
        final double pA = this.getWeight() / weight_of_sum;
        final double pB = B.getWeight() / weight_of_sum;
        final SimpleMatrix mean_of_sum = this.getMean().scale(pA).plus(B.getMean().scale(pB));
        final SimpleMatrix covariance_of_sum = this.getCov().scale(pA).plus(B.getCov().scale(pB)).plus(this.getMean().minus(B.getMean()).scale(pA * pB).mult(this.getMean().minus(B.getMean()).transpose()));

        this.setMean(mean_of_sum);
        this.setCovariance(covariance_of_sum);
        this.setWeight(weight_of_sum);
        this.setCardinality(this.getCardinality() + B.getCardinality());

        if (add) {
            data.add(B);
        }
        return this;
    }

    public Cluster add(ClusterLike p) {
        return add(p, true);
    }

    public Cluster addPoint(ClusterLike p) {
        data.add(p);
        return this;
    }

    public static Cluster sub(Cluster A, Cluster B) {//A-B
        Cluster ret = new Cluster();
        ret.setWeight(A.getWeight() - B.getWeight());
        final double pA = A.getWeight() / ret.getWeight();
        final double pB = B.getWeight() / ret.getWeight();
        ret.setMean(A.getMean().scale(pA).minus(B.getMean().scale(pB)));
        ret.setCovariance(A.getCov().scale(pA).minus(B.getCov().scale(pB)).minus(A.getMean().minus(B.getMean()).scale(pA * pB).mult(A.getMean().minus(B.getMean()).transpose())));
        ret.setCardinality(A.getCardinality() - B.getCardinality());
        return ret;
    }

    public Cluster sub(ClusterLike B, boolean add) {//A-B
        final double weight_of_sub = this.getWeight() - B.getWeight();
        final double pA = this.getWeight() / weight_of_sub;
        final double pB = B.getWeight() / weight_of_sub;
        final SimpleMatrix mean_of_sub = this.getMean().scale(pA).minus(B.getMean().scale(pB));
        final SimpleMatrix covariance_of_sub = this.getCov().scale(pA).minus(B.getCov().scale(pB)).minus(this.getMean().minus(B.getMean()).scale(pA * pB).mult(this.getMean().minus(B.getMean()).transpose()));

        this.setMean(mean_of_sub);
        this.setCovariance(covariance_of_sub);
        this.setWeight(weight_of_sub);
        this.setCardinality(this.getCardinality() - B.getCardinality());

        if (add) {
            data.remove(data.indexOf(B));
        }
        return this;
    }

    public Cluster sub(ClusterLike p) {
        return sub(p, true);
    }

    public Cluster subPoint(ClusterLike p) {
        data.remove(p);
        return this;
    }

    public int getDimension() {
        if (mean != null) {
            return mean.numRows();
        }
        return -1;
    }

    public double getCost() {
        if(cardinality == 0) return 0;
        return costFunction.h();
    }

    public List<ClusterLike> getData() {
        return data;
    }

    public void setData(List<ClusterLike> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        this.data.clear();
        this.weight = 0.;
        this.cardinality = 0;
        this.mean = new SimpleMatrix(getDimension(), 1);
        this.cov = new SimpleMatrix(getDimension(), getDimension());
    }

    public Cost getType() {
        return costFunction;
    }

}
