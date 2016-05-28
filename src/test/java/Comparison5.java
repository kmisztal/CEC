import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class Comparison5 extends ComparisonWithR {
    private String filePath = "src/main/resources/datat/comparison_with_r/input5.txt";
    private double[][] centers = {
            {13.55774, 15.80755, 2.768868},
            {12.69625, 19.57375, 1.862875},
            {12.88556, 23.69778, 1.454000}
    };
    private double[][][] covariances = {
            {
                    {0.48449676, -0.05126593, 0.36235404},
                    {-0.05126593, 3.04749021, -0.01278391},
                    {0.36235404, -0.01278391, 0.51785155}
            },
            {
                    {0.59074594, -0.02739844, -0.03520922},
                    {-0.02739844, 1.34393594, 0.06722547},
                    {-0.03520922, 0.06722547, 0.85159548}
            },
            {
                    {0.4279758, -0.2534543, -0.2503711},
                    {-0.2534543, 4.1997728, 0.9384533},
                    {-0.2503711, 0.9384533, 0.7760907}
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
