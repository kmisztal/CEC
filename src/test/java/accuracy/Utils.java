package accuracy;

import cec.cluster.Cluster;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jkordas on 24/05/16.
 */
public class Utils {
    public static Map<Integer, Integer> findMapping(List<Cluster> clusters, double[][] centers) {

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
