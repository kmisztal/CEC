package stats;

import stats.utils.LogLikelihoodFunction;

/**
 * Created by Krzysztof on 30.11.2016.
 */
public class AICScore implements ClusterEvaluation {
    @Override
    public double score(CECResult result) {
        // number of free parameters K
        final double k = result.getModelsComplexity();
        // loglikelihood log(L)
        final double l = LogLikelihoodFunction.loglikelihood(result);
        // AIC score
        final double aic = -2 * l + 2 * k;
        return aic;
    }

    @Override
    public boolean compareScore(double score1, double score2) {
        // should be minimalized
        return Math.abs(score2) < Math.abs(score1);
    }
}
