package accuracy;

import cec.CEC;
import cec.cluster.Cluster;
import cec.cluster.types.ClusterKind;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.NormOps;
import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RetryRule;

import java.io.IOException;
import java.util.HashMap;
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
    private static final Logger logger = LoggerFactory.getLogger(ComparisonWithR.class);
    private final double EPSILON = 0.01;
    private final int REPEAT = 10;

    public final String INPUT_FILES_DIR = "src/main/resources/datat/comparison_with_r/";

    @Rule
    public RetryRule retry = new RetryRule(4);

    private CEC cec;

    public abstract String getFilePath();

    public abstract double[][] getCenters();
    private double[][] centers;

    public abstract double[][][] getCovariances();
    private double[][][] covariances;

    public abstract ClusterKind getClusterKind();

    @Before
    public void setUp() throws Exception {
        //R output centers
        centers = getCenters();
        //R output covariances
        covariances = getCovariances();

        cec = new CEC();
        cec.setData(getFilePath(), "text/space-separated-values");
        cec.add(getClusterKind(), REPEAT);
    }

    public void shouldFailedWhenTheResultsFromTheRAreNotTheSameAsForCEC() throws IOException {
        cec.run();
//        cec.showResults();

        //create list of non empty clusters
        List<Cluster> clusters = cec.getResult().getClusters().stream().filter(c -> !c.isEmpty()).collect(Collectors.toList());
        assertEquals(centers.length, cec.getResult().getClusters().stream().filter(c -> !c.isEmpty()).count());

        //create appropriate centers mapping
        Map<Integer, Integer> clustersActualToExpectedMapping = findMapping(clusters, centers);
        assertEquals(centers.length, clustersActualToExpectedMapping.size());


        //check covariance matrix norm difference
        int num = 0;
        for (Cluster cluster : clusters) {
            DenseMatrix64F covarianceMatrix = new DenseMatrix64F(covariances[num++]);

            double norm = NormOps.normF(cluster.getCov().getMatrix());
            double expectedNorm = NormOps.normF(covarianceMatrix);
            double diff = Math.abs(norm - expectedNorm);

            logger.info("Difference : {}", diff);

            assertThat("Difference", diff, lessThan(EPSILON));
        }

    }

    private static Map<Integer, Integer> findMapping(List<Cluster> clusters, double[][] centers) {

        Map<Integer, Integer> clustersActualToExpectedMapping = new HashMap<>();

        //identify centres
        int num = 0;

        for (Cluster cluster : clusters) {
            int mappingNumber = -1;
            double distance = Double.MAX_VALUE;

            for (int j = 0; j < centers.length; j++) {
                double[] center = centers[j];
                SimpleMatrix centerMatrix = new SimpleMatrix(center.length,1,true,center);
                SimpleMatrix result = centerMatrix.minus(cluster.getMean());
                result = result.transpose().mult(result);

                if(result.get(0) < distance) {
                    distance = result.get(0);
                    mappingNumber = j;
                }
            }

            clustersActualToExpectedMapping.put(num++, mappingNumber);
        }

        return clustersActualToExpectedMapping;
    }
}
