import cec.CEC;
import cec.cluster.Cluster;
import cec.cluster.types.ClusterKind;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.NormOps;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by jkordas on 24/05/16.
 */
public abstract class ComparisonWithR {
    public final double EPSILON = 0.01;
    public final int REPEAT = 10;

    public abstract String getFilePath();

    public abstract double[][] getCenters();

    public abstract double[][][] getCovariances();

    public abstract ClusterKind getClusterKind();

    public void simpleTest() throws IOException {
        //test input data path
        String filePath = getFilePath();
        //R output centers
        double[][] centers = getCenters();
        //R output covariances
        double[][][] covariances = getCovariances();

        CEC cec = new CEC();
        cec.setData(filePath, "text/space-separated-values");
        cec.add(getClusterKind(), REPEAT);

        cec.run();
//        cec.showResults();

        //create list of non empty clusters
        List<Cluster> clusters = cec.getResult().getClusters().stream().filter(c -> !c.isEmpty()).collect(Collectors.toList());
        assertEquals(centers.length, cec.getResult().getClusters().stream().filter(c -> !c.isEmpty()).count());

        //create appropriate centers mapping
        Map<Integer, Integer> clustersActualToExpectedMapping = Utils.findMapping(clusters, centers);
        assertEquals(centers.length, clustersActualToExpectedMapping.size());


        //check covariance matrix norm difference
        int num = 0;
        for (Cluster cluster : clusters) {
            DenseMatrix64F covarianceMatrix = new DenseMatrix64F(covariances[num++]);

            double norm = NormOps.normF(cluster.getCov().getMatrix());
            double expectedNorm = NormOps.normF(covarianceMatrix);
            double diff = Math.abs(norm - expectedNorm);

            System.out.println("Difference : " + diff);

            assertThat("Difference", diff, lessThan(EPSILON));
        }

    }
}
