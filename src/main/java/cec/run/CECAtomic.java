package cec.run;

import cec.cluster.Cluster;
import cec.cluster.ClusterLike;
import cec.cluster.Point;
import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 *
 * @author Krzysztof
 */
public class CECAtomic {

    private final Random rand = new Random();

    private final Data data;
    private final List<Pair<ClusterKind, TypeOptions>> clusterTypes;
    private final int iterations;
    private final int SIZE_MIN;
    /**
     * cost per iteration
     */
    private final List<Double> iterationsCosts;
    private final ArrayList<Cluster> clusters;
    /**
     * cost per cluster
     */
    private final double[] cost;
    private final int numberOfClusters;

    CECAtomic(Data data, List<Pair<ClusterKind, TypeOptions>> clusterTypes, int iterations) {
        this.data = data;
        this.clusterTypes = clusterTypes;
        this.iterations = iterations;
        this.iterationsCosts = new ArrayList<>();

        this.numberOfClusters = clusterTypes.size();
        this.cost = new double[numberOfClusters];
        this.clusters = new ArrayList<>();

        this.SIZE_MIN = 10;//1 * data.getSize() / 400;

        fillClusters();
    }

    public double getCost() {
        return DoubleStream.of(cost).sum();
    }

    public List<Double> getIterationsCosts() {
        return iterationsCosts;
    }

    private void fillClusters() {
        clusterTypes.forEach((Pair<ClusterKind, TypeOptions> p) -> {
            if (p.getKey().isOptionNeeded()) {
                clusters.add(new Cluster(p.getKey().getFunction().setOptions(p.getValue()), data.getDimension()));
            } else {
                clusters.add(new Cluster(p.getKey().getFunction(), data.getDimension()));
            }
        });
    }

    public int getDimension() {
        return data.getDimension();
    }

    public Data getData() {
        return data;
    }

    /**
     *
     * @return list of clusters
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * initialize the CEC
     * - set id for a clusters
     * - randomly set points to cluster
     * - calculate initial cost
     */
    private void init() {

        for (int i = 0; i < clusters.size(); ++i) {
            clusters.get(i).setId(i);
        }

        //data.getData().forEach((p) -> clusters.get(rand.nextInt(numberOfClusters)).add(p));

        //alternative
        Point[] centroids = new Point[clusters.size()];
        for (int i = 0; i < clusters.size(); ++i) {
            centroids[i] = data.getData().get(rand.nextInt(data.getSize()));
        }
        data.getData().forEach((p) -> clusters.get(getNearest(centroids, p)).add(p));

        //end alternative

        for (int i = 0; i < numberOfClusters; ++i) {
            cost[i] = clusters.get(i).getCost();
        }
    }

    private int getNearest(Point[] centroids, Point p) {
        int min = 0;
        double dist_min = Double.MAX_VALUE;
        for (int i = 0; i < centroids.length; ++i) {
            final double dist = p.dist(centroids[i]);
            if (dist < dist_min) {
                min = i;
                dist_min = dist;
            }

        }
        return min;
    }

    private boolean iteration() {
        final double cost_ret = getCost();

        for (Cluster Yj : clusters) {
            for (ClusterLike p : Yj.getData()) {
                final double Yj_cost_sub = Yj.sub(p, false).getCost();
                int best_cluster = Yj.getId();
                double best_cost = Double.MIN_VALUE;

                for (Cluster Yi : clusters) {
                    if (Yi.isEmpty() || Yi.getId() == Yj.getId()) {
                        continue;
                    }

                    final double local_cost = cost[Yj.getId()] + cost[Yi.getId()] - Yi.add(p, false).getCost() - Yj_cost_sub;

                    if (local_cost > 0 && best_cost < local_cost) {
                        best_cost = local_cost;
                        best_cluster = Yi.getId();
                    }
                    Yi.sub(p, false);
                }

                if (Yj.getId() != best_cluster) {
                    cost[Yj.getId()] = Yj.subPoint(p).getCost();//
                    cost[best_cluster] = clusters.get(best_cluster).add(p).getCost();
                } else {
                    Yj.add(p, false);
                }

                //delete cluster
                if (Yj.isEmpty() || (!Yj.isEmpty() && Yj.getCardinality() < SIZE_MIN)) {
                    Yj.getData().forEach((p_del) -> clusters.get(getRandomCluster(Yj.getId())).add(p_del));

                    Yj.clear();

                    //update cost
                    for (int i = 0; i < numberOfClusters; ++i) {
                        cost[i] = clusters.get(i).getCost();
                    }
                    break;
                }
            }
        }
        return cost_ret != getCost();
    }

    private int getRandomCluster(int x) {
        int ret;
        do {
            ret = rand.nextInt(numberOfClusters);
        } while (x == ret || clusters.get(ret).isEmpty());
        return ret;
    }

    public void run() {
        //initialization
        init();

        for (int i = 0; i < iterations; ++i) {
            final boolean t = iteration();
            iterationsCosts.add(getCost());
            if (!t) {
                break;
            }
            
            //zmiana na dzielenie co ileś pętli
        }
    }

    /**
     * 
     * @return initial number of clusters (it can differ from result clusters needed for data description)
     */
    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    /**
     * 
     * @return cluster needed for fully describe data
     */
    public int getUsedNumberOfClusters() {
        return (int) clusters.stream().filter(p -> p.isEmpty()).count();
    }

}
