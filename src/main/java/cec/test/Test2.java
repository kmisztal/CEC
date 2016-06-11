package cec.test;

import cec.CEC;

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
    public static void main(String[] args) throws IOException {


        CEC cec = new CEC();
        cec.run();
        cec.showResults();
        cec.saveResults();
    }

}