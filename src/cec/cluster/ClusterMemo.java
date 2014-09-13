package cec.cluster;

import cec.CECRunner;
import cec.cluster.types.ClusterKind;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Krzysztof
 */
public class ClusterMemo {

    private static ClusterMemo singleton;
    private final List<ClusterKind> clusterTypes;
    private final List<Cluster> clusters;
    private static CECRunner cec;
    private double bestCost = Double.MAX_VALUE;

    private ClusterMemo(CECRunner aThis) {
        clusterTypes = new CopyOnWriteArrayList<>();
        clusters = new CopyOnWriteArrayList<>();
        ClusterMemo.cec = aThis;
    }

    public static ClusterMemo getInstance(CECRunner aThis) {
        if (singleton == null) {
            singleton = new ClusterMemo(aThis);
        } else {
            ClusterMemo.cec = aThis;
        }
        return singleton;
    }

    public void clear() {
        throw new RuntimeException("Not implemtned yet.");
    }

    public void add(ClusterKind clusterKind) {
        this.clusterTypes.add(clusterKind);
    }

    public void init() {
        cec.clearClusters();
        
        clusterTypes.stream().forEach((c) -> {
            cec.add(c);
        });
//        cec.setNumberOfClusters(clusterTypes.size());
    }

    public void saveBest() {
        if (clusters.isEmpty() || cec.getCost() < this.bestCost) {
            System.out.println("Better " + this.bestCost + " > " +cec.getCost());
            this.bestCost = cec.getCost();
            cec.getCLusters().stream().forEach((c) -> {
                clusters.add(c);
            });
            

        }
    }
}
