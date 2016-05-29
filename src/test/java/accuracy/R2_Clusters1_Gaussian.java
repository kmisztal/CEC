package accuracy;

import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class R2_Clusters1_Gaussian extends ComparisonWithR {
    private final String FILE_PATH = INPUT_FILES_DIR + "input3.txt";
    private final double[][] CENTERS = {
            {-0.006448612, 0.009184474}
    };
    private final double[][][] COVARIANCES = {
            {
                    {0.207799834, 0.001313875},
                    {0.001313875, 0.200261684}
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
