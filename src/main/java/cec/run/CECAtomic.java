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
    private List<Pair<ClusterKind, TypeOptions>> clusterTypes;
    private final int iterations;
    private final int SIZE_MIN;
    /**
     * cost per iteration
     */
    private final List<Double> costs;
    private ArrayList<Cluster> clusters;
    /**
     * cost per cluster
     */
    private double[] cost;
    private int numberOfClusters;

    CECAtomic(Data data, List<Pair<ClusterKind, TypeOptions>> clusterTypes, int iterations) {
        this.data = data;
        this.clusterTypes = clusterTypes;
        this.iterations = iterations;
        this.costs = new ArrayList<>();

        this.numberOfClusters = clusterTypes.size();
        this.cost = new double[numberOfClusters];
        this.clusters = new ArrayList<>();

        this.SIZE_MIN = 1 * data.getSize() / 100;

        fillClusters();
    }

    public double getCost() {
        return DoubleStream.of(cost).sum();
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

        data.getData().forEach((p) -> clusters.get(rand.nextInt(numberOfClusters)).add(p));

        for (int i = 0; i < numberOfClusters; ++i) {
            cost[i] = clusters.get(i).getCost();
        }
    }

    private boolean divide() {
        ArrayList<Cluster> newClusters = new ArrayList<>();
        List<Pair<ClusterKind, TypeOptions>> newTypes = new ArrayList<>();
        boolean divided = false;
        int empties = 0;
        for (Cluster cl : clusters) {
            if (cl.isEmpty()) {
                ++empties;
                continue;
            }
            if (cl.getCardinality() < 2*SIZE_MIN)
                continue;
            double prev_cost = cl.getCost();
            List<Pair<ClusterKind, TypeOptions>> params = new ArrayList<>();
            params.add(clusterTypes.get(cl.getId()));
            params.add(clusterTypes.get(cl.getId()));
            CECAtomic best_result = null;
            Data loc = new Data(cl.getData());
            for (int i = 0; i < 16; ++i) {
                final CECAtomic result = new CECAtomic(loc, params, 50);

                result.simpleRun();

                if ((best_result == null || best_result.getCost() > result.getCost())
                        && result.getClusters().get(0).getCardinality() > SIZE_MIN
                        && result.getClusters().get(1).getCardinality() > SIZE_MIN) {
                    best_result = result;
                }
            }
            if (best_result != null && 0.001*this.getCost() < (prev_cost-best_result.getCost())) {
                newClusters.addAll(best_result.getClusters());
                newTypes.addAll(params);
                cl.clear();
                divided = true;
            }
        }
        if (divided) {
            double[] newCosts = new double[clusters.size()+newClusters.size()-empties];
            int i;
            for (i=0;i<newClusters.size();++i) {
                newCosts[i] = newClusters.get(i).getCost();
                newClusters.get(i).setId(i);
            }
            for (Cluster cl : clusters) {
                if (!cl.isEmpty()) {
                    newCosts[i] = cl.getCost();
                    newClusters.add(cl);
                    newTypes.add(clusterTypes.get(cl.getId()));
                    cl.setId(i);
                    ++i;
                }
            }
            this.clusters = newClusters;
            this.clusterTypes = newTypes;
            this.numberOfClusters = newClusters.size();
            this.cost = newCosts;
        }

        return divided;
    }

    private boolean merge() {
        for (Cluster cl1 : clusters) {
            if (cl1.isEmpty())
                continue;
            for (Cluster cl2 : clusters) {
                if (cl2.isEmpty() || cl1.getId() == cl2.getId())
                    continue;
                if (cl1.getType().getClass() == cl2.getType().getClass() && cl1.getId() > cl2.getId())
                    continue;
                Cluster merged = new Cluster(clusterTypes.get(cl2.getId()).getKey().getFunction() , this.getDimension());
                merged.getCostFunction().setCluster(merged);
                for (ClusterLike point : cl1.getData()) {
                    merged.add(point, true);
                }
                for (ClusterLike point : cl2.getData()) {
                    merged.add(point, true);
                }
                if (merged.getCost() < cl1.getCost() + cl2.getCost()) {
                    for (ClusterLike point : cl2.getData())
                        cl1.add(point, true);
                    cl2.clear();
                    return true;
                }
            }
        }
        return false;
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
            boolean t = iteration();
            costs.add(getCost());
            while (merge())
                t = true;
            if (!t && !divide()) {
                break;
            }
        }
    }

    public void simpleRun() {
        //initialization
        init();

        for (int i = 0; i < iterations; ++i) {
            final boolean t = iteration();
            if (!t) {
                break;
            }
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
        System.out.println("Completed in " + costs.size() + " steps");
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
            System.out.println(c.getCov());
        }
    }
}
