package accuracy;

import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class R4_Clusters3_Gaussians extends ComparisonWithR {
    private final String FILE_PATH = INPUT_FILES_DIR + "input6.txt";
    private final double[][] CENTERS = {
            {5.006000, 3.428000, 1.462000, 0.246000},
            {6.554545, 2.950909, 5.489091, 1.989091},
            {5.904444, 2.775556, 4.193333, 1.293333}};
    private final double[][][] COVARIANCES = {
            {
                    {0.121764, 0.097232, 0.016028, 0.010124},
                    {0.097232, 0.140816, 0.011464, 0.009112},
                    {0.016028, 0.011464, 0.029556, 0.005948},
                    {0.010124, 0.009112, 0.005948, 0.010884}
            },
            {
                    {0.37847934, 0.09085950, 0.29204959, 0.05568595},
                    {0.09085950, 0.10940826, 0.08146446, 0.05455537},
                    {0.29204959, 0.08146446, 0.31479008, 0.06733554},
                    {0.05568595, 0.05455537, 0.06733554, 0.08206281}
            },
            {
                    {0.27153580, 0.09499753, 0.17714074, 0.05091852},
                    {0.09499753, 0.09295802, 0.08961481, 0.04228148},
                    {0.17714074, 0.08961481, 0.19128889, 0.05706667},
                    {0.05091852, 0.04228148, 0.05706667, 0.03040000}
            }
    };
    private final ClusterKind CLUSTER_KIND = ClusterKind.Gaussians;

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    @Override
    public double[][] getCenters() {
        return CENTERS;
    }

    @Override
    public double[][][] getCovariances() {
        return COVARIANCES;
    }

    @Override
    public ClusterKind getClusterKind() {
        return CLUSTER_KIND;
    }

    @Test
    public void gaussianTest() throws IOException {
        shouldFailedWhenTheResultsFromTheRAreNotTheSameAsForCEC();
    }
}
