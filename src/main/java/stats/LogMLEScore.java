package stats;

import stats.utils.LogLikelihoodFunction;

/**
 * Created by Krzysztof on 07.12.2016.
 */
public class LogMLEScore implements ClusterEvaluation {
    @Override
    public double score(CECResult result) {
        return LogLikelihoodFunction.loglikelihood(result);
    }

    @Override
    public boolean compareScore(double score1, double score2) {
        // should be miximalized
        return score1 < score2;
    }
}