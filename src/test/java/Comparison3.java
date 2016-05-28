import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class Comparison3 extends ComparisonWithR {
    private String filePath = "src/main/resources/datat/comparison_with_r/input3.txt";
    private double[][] centers = {
            {-0.006448612, 0.009184474}
    };
    private double[][][] covariances = {
            {
                    {0.207799834, 0.001313875},
                    {0.001313875, 0.200261684}
            }
    };
    private ClusterKind clusterKind = ClusterKind.Gaussians;

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
