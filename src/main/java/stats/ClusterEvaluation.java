package stats;


/**
 * Created by Krzysztof on 30.11.2016.
 */
public interface ClusterEvaluation {
    /**
     * Returns the score the current clusterer obtains on the dataset.
     *
     * @param result the dataset to test the clusterer on.
     * @return the score the clusterer obtained on this particular dataset
     */
    double score(CECResult result);

    /**
     * Compares the two scores according to the criterion in the implementation.
     * Some score should be maxed, others should be minimized. This method
     * returns true if the second score is 'better' than the first score.
     *
     * @param score1 the first score
     * @param score2 the second score
     * @return true if the second score is better than the first, false in all
     * other cases
     */
    boolean compareScore(double score1, double score2);
}
