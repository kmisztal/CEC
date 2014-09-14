package cec.run;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import cec.options.CECConfig;
import java.util.List;
import java.util.concurrent.Callable;
import javafx.util.Pair;

/**
 *
 * @author Krzysztof
 */
public class CECThread implements CECInterface, Callable<CECAtomic> {

    private final int id;
    private Data data = null;
    private List<Pair<ClusterKind, TypeOptions>> clusterTypes;
    private CECConfig options;

    public CECThread(int id) {
        this.id = id;
    }

    @Override
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public void setClusterTypes(List<Pair<ClusterKind, TypeOptions>> clusterTypes) {
        this.clusterTypes = clusterTypes;
    }

    public void setOptions(CECConfig op) {
        this.options = op;
    }

    public int getId() {
        return id;
    }

    @Override
    public CECAtomic call() throws Exception {
        final int nstart = options.getNStart() / options.getGeneralCores();
        final int iter = options.getIterations();
        CECAtomic best_result = null;
        for (int i = 0; i < nstart; ++i) {
            final CECAtomic result = new CECAtomic(data, clusterTypes, iter);

            result.run();

            if (best_result == null || best_result.getCost() > result.getCost()) {
                best_result = result;
                System.out.println(getId() + ": " + best_result.getCost());
            }
        }
        return best_result;
    }

}
