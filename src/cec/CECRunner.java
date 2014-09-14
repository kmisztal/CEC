package cec;

import cec.cluster.Cluster;
import cec.cluster.ClusterLike;
import cec.cluster.ClusterMemo;
import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOption;
import cec.input.Data;
import cec.input.draw.DataDraw;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Krzysztof
 */
public class CECRunner {

    private int numberOfClusters;
    private boolean[] emptyClusters;
    private final ArrayList<Cluster> clusters;
    private double[] cost;
    private final Data data;
    private final Random rand = new Random();
    private boolean rerun = false;

    public CECRunner(Data data) {
        this.clusters = new ArrayList<>();
        this.data = data;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filename = "data/mouse_fix_r_1/input.txt";

        Data data = new Data();
        data.read(filename, "text/space-separated-values");

        CECRunner cec = new CECRunner(data);

        cec.add(ClusterKind.Gaussian, 6);
        
        cec.run();

//        DataDraw dd = new DataDraw(cec);
//        dd.disp();
    }

    public void add(ClusterKind clusterKind) {
        clusters.add(new Cluster(clusterKind.getFunction(), data.getDimension()));
        ++numberOfClusters;
        cost = new double[numberOfClusters];
        this.emptyClusters = new boolean[numberOfClusters];
    }

    public void add(ClusterKind clusterKind, int repeat) {
        for (int i = 0; i < repeat; ++i) {
            add(clusterKind);
        }
    }
    
    public void add(ClusterKind clusterKind, int repeat, TypeOption options) {
        
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public void run() {
        reset();

        System.out.println(Arrays.toString(job()));
        rerun = true;
        for (int i = 0; i < 10 - 1; i++) {
            System.out.println(Arrays.toString(job()));
        }
    }

    private double[] job(int events) {
        //cost over iterations
        double ret[] = new double[events + 1];
        Arrays.fill(ret, -1.);

        //initialization
        init();
        ret[0] = getCost();

        for (int i = 0; i < events; ++i) {
            final boolean t = iteration();
            ret[i + 1] = getCost();
            if (!t) {
                break;
            }
        }

        clusters.stream().forEach(p -> {
            System.out.println(p.getCardinality());
        });
        return ret;
    }

    private double[] job() {
        return job(100);
    }

    public double[] singleRun() {
        return job();
    }

    public double[] singleRun(int events) {
        return job(events);
    }

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
        double cost_ret = getCost();
        final int SIZE_MIN = (int) (data.getData().size() * 0.01);
//        clusters.stream().forEach((Yj) -> {
        for (Cluster Yj : clusters) {
//            Yj.getData().stream().forEach((p) -> {
            for (ClusterLike p : Yj.getData()) {
                final double Yj_cost_sub = Yj.sub(p, false).getCost();
                int best_cluster = Yj.getId();
                double best_cost = Double.MIN_VALUE;

                for (Cluster Yi : clusters) {
                    if (Yi.getId() == Yj.getId() || Yi.isEmpty()) {
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
                if (Yj.getCardinality() > 0 && Yj.getCardinality() < SIZE_MIN) {

                    System.out.println("DELETE CLUSTER No: " + Yj.getId() + " TYPE: " + Yj.getType());
                    Yj.getData().stream().forEach((p_del) -> {
                        clusters.get(getRandomCluster()).add(p_del);
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

    private int getRandomCluster() {
        int ret = 0;
        do {
            ret = rand.nextInt(numberOfClusters);
        } while (clusters.get(ret).isEmpty());
        return ret;
    }

    public int getDimension() {
        return data.getDimension();
    }

    public Data getData() {
        return data;
    }

    /**
     *
     * @return overall cost
     */
    public double getCost() {
        double s = 0;
        for (double d : cost) {
            s += d;
        }
        return s;
    }

    /**
     *
     * @return list of cluters
     */
    public List<Cluster> getCLusters() {
        return clusters;
    }

    /**
     *
     * @return list of boolean: if true then current cluster is empty
     */
    public boolean[] getEmptyClusters() {
        return emptyClusters;
    }

    private void reset() {
        this.numberOfClusters = 0;
        this.emptyClusters = new boolean[numberOfClusters];
    }

    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    public void clearClusters() {
        clusters.clear();
        numberOfClusters = 0;
    }

}
