package cec.run;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import cec.options.CECConfig;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krzysztof
 */
public class CECExecutor {

    private static CECExecutor singleton;
    private Data data;
    private List<Pair<ClusterKind, TypeOptions>> clusterTypes;
    private CECAtomic best_result;

    private CECExecutor() {
    }

    public static CECExecutor getInstance() {
        if (CECExecutor.singleton == null) {
            CECExecutor.singleton = new CECExecutor();
        }
        return singleton;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setClusterTypes(List<Pair<ClusterKind, TypeOptions>> clusterTypes) {
        this.clusterTypes = clusterTypes;
    }

    public void run(CECConfig op) {
        final int cores = op.getGeneralCores();
        final ExecutorService threadPool = Executors.newFixedThreadPool(cores);

        final CompletionService<CECAtomic> pool = new ExecutorCompletionService<>(threadPool);

        for (int i = 0; i < cores; i++) {
            CECThread cect = new CECThread(i);
            cect.setData(data);
            cect.setClusterTypes(clusterTypes);
            cect.setOptions(op);
            
            pool.submit(cect);
        }

        this.best_result = null;

        for (int i = 0; i < cores; i++) {
            try {
                final CECAtomic result = pool.take().get();

                if (best_result == null || best_result.getCost() > result.getCost()) {
                    best_result = result;
                }
                //Compute the result
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(CECExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        threadPool.shutdown();
    }

    public CECAtomic getBestResult() {
        return best_result;
    }

}
