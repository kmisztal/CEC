package cec.run;

import cec.cluster.Cluster;
import cec.cluster.ClusterLike;
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
    final int SIZE_MIN;
    /**
     * cost per iteration
     */
    private final List<Double> costs;
    private final ArrayList<Cluster> clusters;
    /**
     * cost per cluster
     */
    private final double[] cost;
    private final int numberOfClusters;

    public CECAtomic(Data data, List<Pair<ClusterKind, TypeOptions>> clusterTypes, int iterations) {
        this.data = data;
        this.clusterTypes = clusterTypes;
        this.iterations = iterations;
        this.costs = new ArrayList<>();

        this.numberOfClusters = clusterTypes.size();
        this.cost = new double[numberOfClusters];
        this.clusters = new ArrayList<>();

        this.SIZE_MIN = data.getSize() / 100;

        fillClusters();
    }

    public double getCost() {
//        double s = 0;
//        for (double d : cost) {
//            s += d;
//        }
//        return s;
        return DoubleStream.of(cost).sum();
    }

    private void fillClusters() {
        clusterTypes.stream().forEach((Pair<ClusterKind, TypeOptions> p) -> {
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
     * @return list of cluters
     */
    public List<Cluster> getCLusters() {
        return clusters;
    }

    /**
     * initialize the CEC
     * - set id for a clusters
     * - randomly set points to cluster
     * - calcualte initial cost
     */
    private void init() {

        for (int i = 0; i < clusters.size(); ++i) {
            clusters.get(i).setId(i);
        }

        data.getData().stream().forEach((p) -> {
            clusters.get(rand.nextInt(numberOfClusters)).add(p);
        });

        for (int i = 0; i < numberOfClusters; ++i) {
            cost[i] = clusters.get(i).getCost();
        }
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
                if (!Yj.isEmpty() && Yj.getCardinality() < SIZE_MIN) {
                    Yj.getData().stream().forEach((p_del) -> {
                        clusters.get(getRandomCluster(Yj.getId())).add(p_del);
                    });

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
        int ret = 0;
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
            costs.add(getCost());
            if (!t) {
                break;
            }
            
            //zmiana na dzielenie co ileś pętli
        }
    }

    /**
     * 
     * @return initail number of clusters (it can differ from result clasuters needed for data description)
     */
    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    /**
     * 
     * @return cluster needed for fully describe data
     */
    private int getUsedNumberOfClusters() {
        int ret = 0;
        ret = clusters.stream().filter((c) -> (!c.isEmpty())).map((_item) -> 1).reduce(ret, Integer::sum);
        return ret;
    }

    /**
     * prints the results on console
     */
    public void showResults() {
        System.out.println("");
        System.out.println("BEST RUN INFO");
        System.out.println("Completted in " + costs.size() + " steps");
        System.out.println("Cost in each step " + costs.toString());
        final int v = getUsedNumberOfClusters();
        System.out.println(v + " needed for clustering (while " + numberOfClusters + " suggested)");

        int no = 0;
        for (Cluster c : clusters) {
            if (c.isEmpty()) {
                continue;
            }
            System.out.println("------------------------------------------------------------------");
            System.out.println("Cluster " + no++ + " " + c.getType());
            System.out.println(c.getCardinality() + " points");
            System.out.println("mean");
            System.out.println(c.getMean());
            System.out.println("cov");
            System.out.println(c.getCostFunction().getCov());
        }
    }
}
