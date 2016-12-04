package stats;

import cec.cluster.Cluster;
import cec.cluster.Point;
import cec.run.CECAtomic;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krzysztof on 30.11.2016.
 */
public class CECResult {
    private final CECAtomic result;

    public CECResult(CECAtomic result) {
        this.result = result;
    }

    public int getDataSize() {
        return result.getData().getSize();
    }

    public int getDimension() {
        return result.getData().getDimension();
    }

    public List<Point> getData() {
        return result.getData().getData();
    }

    public List<Cluster> getClusters() {
        return result.getClusters();
    }

    /**
     * prints the results on console
     */
    public void showResults() {
        System.out.println("");
        System.out.println("BEST RUN INFO");
        System.out.println("Completed in " + result.getIterationsCosts().size() + " steps");
        System.out.println("Cost in each step " + result.getIterationsCosts().toString());
        final int v = result.getUsedNumberOfClusters();
        System.out.println(v + " needed for clustering (while " + result.getNumberOfClusters() + " suggested)");

        int no = 0;
        for (Cluster c : result.getClusters()) {
            if (c.isEmpty()) {
                continue;
            }
            System.out.println("------------------------------------------------------------------");
            System.out.println("Cluster " + no++ + " " + c.getType());
            System.out.printf("%d points (%.6f)\n", c.getCardinality(), (1. * c.getCardinality() / result.getData().getSize()));
            System.out.println("mean");
            System.out.println(c.getMean());
            System.out.println("covariance matrix thoretical");
            System.out.println(c.getCostFunction().getCov());
            System.out.println("covariance matrix empirical");
            System.out.println(c.getCov());
        }
    }

    public int getNumberOfClusters() {
        return result.getNumberOfClusters();
    }

    public int getUsedNumberOfClusters() {
        return result.getUsedNumberOfClusters();
    }

    public ArrayList<Integer> getPartition() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (Point p : result.getData().getData()) {
            for (Cluster c : result.getClusters()) {
                if (c.getData().contains(p)) {
                    ret.add(c.getId());
                    break;
                }
            }
        }

        return ret;
    }

    public void savePartition(String s) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(s)) {
            for (int i : getPartition()) {
                out.println(i);
            }
        }
    }

    public int numberOfFreeParameters() {
        int res = 0;
        for (Cluster c : result.getClusters()) {
            if (!c.isEmpty())
                res += c.getType().numberOfFreeParameters();
        }
        return res;
    }
}
