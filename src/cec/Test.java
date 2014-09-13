package cec;

import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOption;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Krzysztof
 */
public class Test {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filename = "data/mouse_fix_r_1/input.txt";
        
        CEC cec = new CEC();
        cec.setData(filename, "text/space-separated-values");

        cec.add(ClusterKind.Gaussian, 3);
        cec.add(ClusterKind.Gaussian, 3, 
                TypeOption.add("lambda1", 2.3),
                TypeOption.add("lambda2", 3.4)
        );

        cec.run();

        //print the 
        //and if it possible you will see the plot
        cec.showResults();
        
        //save results to file
        cec.saveResults();
    }

}
