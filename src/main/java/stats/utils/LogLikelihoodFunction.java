package stats.utils;

import cec.cluster.Cluster;
import cec.cluster.Point;
import stats.CECResult;

/**
 * Created by Krzysztof on 30.11.2016.
 */
public class LogLikelihoodFunction {
    // total likelihood of finding data for given partition
    public static double loglikelihood(CECResult result) {
        double loglikelihood = 0.;
        for (Point p : result.getData()) {
            loglikelihood += loglikelihood(result, p);
        }
        return loglikelihood;
    }

    private static double loglikelihood(CECResult result, Point p) {
        double res = 0.0;
        for (Cluster c : result.getClusters()) {
            if (!c.isEmpty())
                res += c.getPointCost(p);
        }
        return Math.log(res);
    }
}
