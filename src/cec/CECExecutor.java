package cec;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import cec.options.CECConfig;
import cec.options.Options;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Krzysztof
 */
public class CECExecutor {

    private static CECExecutor singleton;
    private Data data;
    private List<Pair<ClusterKind,TypeOptions>> clusterTypes;
    

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

    public void setClusterTypes(List<Pair<ClusterKind,TypeOptions>> clusterTypes) {
        this.clusterTypes = clusterTypes;
    }

    public void run(Options op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void run(CECConfig op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void showResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
