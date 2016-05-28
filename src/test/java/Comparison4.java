import cec.cluster.types.ClusterKind;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jkordas on 24/05/16.
 */
public class Comparison4 extends ComparisonWithR {
    private String filePath = "src/main/resources/datat/comparison_with_r/input4.txt";
    private double[][] centers = {
            {1.842451, 1.914620},
            {-2.676846, -2.877392},
            {1.213444, 1.411880},
            {-1.158407, -1.056242}};
    private double[][][] covariances = {
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
