package cec.run;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import javafx.util.Pair;

import java.util.List;

/**
 *
 * @author Krzysztof
 */
public interface CECInterface {
    public void setData(Data data);

    public void setClusterTypes(List<Pair<ClusterKind,TypeOptions>> clusterTypes);
}
