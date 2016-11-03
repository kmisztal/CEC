package accuracy;

import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class R2_Clusters3_SphericalGaussian extends ComparisonWithR {
    private final String FILE_PATH = INPUT_FILES_DIR + "input2.txt";
    private final double[][] CENTERS = {
            {-1.83541264, 1.82332016},
            {0.02698894, -0.06156843},
            {1.84047584, 1.84429708}
    };
    private final double[][][] COVARIANCES = {
            {
                    {0.2532616, 0.0462408},
                    {0.0462408, 0.2847791}
            },
            {
                    {0.951222433, 0.007425228},
                    {0.007425228, 0.974692632}
            },
            {
                    {0.27692511, -0.04878508},
                    {-0.04878508, 0.26448490}
            }
    };
    private final ClusterKind CLUSTER_KIND = ClusterKind.SphericalGaussians;

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
    public void sphericalGaussianTest() throws IOException {
        shouldFailedWhenTheResultsFromTheRAreNotTheSameAsForCEC();
    }
}
