package stats;

import stats.utils.LogLikelihoodFunction;

/**
 * Created by Krzysztof on 30.11.2016.
 */
public class BICScore implements ClusterEvaluation {
    @Override
    public double score(CECResult result) {
        // number of free parameters K
        final double k = result.getModelsComplexity();
        // sampelsize N
        final double datasize = result.getData().size();

        // loglikelihood log(L)
        final double l = LogLikelihoodFunction.loglikelihood(result);
        // BIC score
        final double bic = -2 * l + Math.log(datasize) * k;
        return bic;
    }

    @Override
    public boolean compareScore(double score1, double score2) {
        // should be minimzed.
        return score2 > score1;
    }
}
