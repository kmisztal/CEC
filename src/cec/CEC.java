package cec;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOption;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import cec.options.CECConfig;
import cec.options.Options;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.util.Pair;

/**
 *
 * @author Krzysztof
 */
public class CEC {

    private Data data;
    private final List<Pair<ClusterKind,TypeOptions>> clusterTypes;
    private String configFile;
    private CECExecutor ecec;
    private CECConfig op;

    /**
     * default constructor
     */
    public CEC() {
        this.clusterTypes = new CopyOnWriteArrayList<>();
    }
    
    public CEC(String [] args) {
        this();
    }

    public void setData(String filename, String type) throws IOException {
        this.data = new Data();
        this.data.read(filename, type);
    }

    public void add(ClusterKind clusterKind, int repeat) {        
        for (int i = 0; i < repeat; ++i) {
            add(clusterKind, null);
        }
    }
    
    public void add(ClusterKind clusterKind, int repeat, TypeOption ... typeOption) {
        for (int i = 0; i < repeat; ++i) {
            add(clusterKind, new TypeOptions(typeOption));
        }
    }
    
    public void add(ClusterKind clusterKind, TypeOptions typeOption) {
        if(clusterKind.isOptionNeeded())
            throw new RuntimeException("The " + clusterKind.name() + " needs additional configuration");
        
        clusterTypes.add(new Pair<>(clusterKind, typeOption));
    }

    public void run() throws IOException {
        if(clusterTypes.isEmpty()){
            System.err.println("READ FROM CONFIG FILE");
        }
        
        if(data == null){
            System.err.println("READ DATA INPUT FROM CONFIG FILE");
        }
        
        
        ecec = CECExecutor.getInstance();
        op = CECConfig.getInstance(this.configFile);
        
        if(data == null){
            setData(op.getGeneralInputFilename(), op.getGeneralInputFileType());
        }
        ecec.setData(data);
        if(clusterTypes.isEmpty()){
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        ecec.setClusterTypes(clusterTypes);
        
        
        ecec.run(op);
    }
    
    public void setConfigFile(String filename){
        this.configFile = filename;
    }

    public void showResults() {
        ecec.showResults();
    }

    public void saveResults() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
