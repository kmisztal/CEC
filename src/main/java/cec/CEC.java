package cec;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOption;
import cec.cluster.types.TypeOptions;
import cec.input.Data;
import cec.options.CECConfig;
import cec.run.CECAtomic;
import cec.run.CECExecutor;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Krzysztof
 */
public class CEC {

    private final List<Pair<ClusterKind,TypeOptions>> clusterTypes;
    private Data data;
    private String configFile;
    private CECExecutor ecec;

    private String[] args;

    /**
     * default constructor
     */
    public CEC() {
        this.clusterTypes = new CopyOnWriteArrayList<>();
        this.args = new String[0];
    }

    public CEC(String [] args) {
        this.args = args;
        this.clusterTypes = new CopyOnWriteArrayList<>();
    }

    public void setData(String filename, String type) {
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
        if(clusterKind.isOptionNeeded() && typeOption.isEmpty())
            throw new RuntimeException("The " + clusterKind.name() + " needs additional configuration");
            
        else
            clusterTypes.add(new Pair<>(clusterKind, typeOption));
    }

    public void run() throws IOException {
        ecec = CECExecutor.getInstance();
        CECConfig op = CECConfig.getInstance(this.configFile, args);
        
        //load data
        if(data == null){
            setData(op.getGeneralInputFilename(), op.getGeneralInputFileType());
        }
        ecec.setData(data);
        
        //load cluster types
        if(clusterTypes.isEmpty()){
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        ecec.setClusterTypes(clusterTypes);
        
        //run
        ecec.run(op);
    }
    
    public void setConfigFile(String filename){
        this.configFile = filename;
    }

    public CECAtomic getResult(){
        return ecec.getBestResult();
    }
    
    public void showResults() {
        getResult().showResults();
    }

    public void saveResults() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Data getData() {
        return data;
    }
}
