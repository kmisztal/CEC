package accuracy;

import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Krzysztof on 09.01.2017.
 */
public class R1_Clusters3_Gaussians extends ComparisonWithR {
    private final String FILE_PATH = "src/main/resources/data_test/csv/gauss3x_in1d.csv";
    private final double[][] CENTERS = {
            {-1},
            {3},
            {6}};
    private final double[][][] COVARIANCES = {
            {
                    {1}
            },
            {
                    {1}
            },
            {
                    {1}
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

    @Override
    public double getEPSILON() {
        return 3.0;
    }

    @Test
    public void gaussianTest() throws IOException {
        shouldFailedWhenTheResultsFromTheRAreNotTheSameAsForCEC();
    }
}
