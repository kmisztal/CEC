package accuracy;

import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class R2_Clusters4_Gaussians extends ComparisonWithR {
    private final String FILE_PATH = INPUT_FILES_DIR + "input4.txt";
    private final double[][] CENTERS = {
            {1.842451, 1.914620},
            {-2.676846, -2.877392},
            {1.213444, 1.411880},
            {-1.158407, -1.056242}};
    private final double[][][] COVARIANCES = {
            {
                    {2.037418, -1.0756265},
                    {-1.075627, 0.5680293}
            },
            {
                    {1.0501789, 0.3995286},
                    {0.3995286, 0.6215309}
            },
            {
                    {3.976852, -2.372146},
                    {-2.372146, 1.518324}
            },
            {
                    {4.746504, 1.4918043},
                    {1.491804, 0.5038512}
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
