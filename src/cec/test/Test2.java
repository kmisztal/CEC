package cec.test;

import cec.CEC;
import cec.cluster.types.ClusterKind;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Krzysztof
 */
public class Test2 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        CEC cec = new CEC(args);
        cec.run();
        cec.showResults();
        cec.saveResults();
    }

}