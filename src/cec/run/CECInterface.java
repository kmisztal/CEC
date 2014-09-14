package cec.run;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Krzysztof
 */
public interface CECInterface {
    public void setData(Data data);

    public void setClusterTypes(List<Pair<ClusterKind,TypeOptions>> clusterTypes);
}
