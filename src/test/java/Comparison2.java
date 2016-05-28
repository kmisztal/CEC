import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class Comparison2 extends ComparisonWithR {
    private String filePath = "src/main/resources/datat/comparison_with_r/input2.txt";
    private double[][] centers = {
            {-1.83541264, 1.82332016},
            {0.02698894, -0.06156843},
            {1.84047584, 1.84429708}
    };
    private double[][][] covariances = {
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
    private ClusterKind clusterKind = ClusterKind.SphericalGaussians;

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public double[][] getCenters() {
        return centers;
    }

    @Override
    public double[][][] getCovariances() {
        return covariances;
    }

    @Override
    public ClusterKind getClusterKind() {
        return clusterKind;
    }

    @Test
    public void test() throws IOException {
        simpleTest();
    }
}
