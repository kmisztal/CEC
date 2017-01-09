package cec.test;

import cec.CEC;
import cec.cluster.types.ClusterKind;

import java.io.IOException;

/**
 * Created by Krzysztof on 09.01.2017.
 */
public class Test1D {
    public static void main(String[] args) throws IOException {
        CEC cec = new CEC();

        String src = "src/main/resources/data_test/csv/gauss3x_in1d.csv";
        cec.setData(src,
                "text/space-separated-values");

        cec.add(ClusterKind.Gaussians, 30);

        cec.run();

        //print the results
        //and if it possible you will see the plot
        cec.showResults();

        //save results to file
        cec.saveResults();

//        new DataDraw(cec.getResult()).disp();

//        cec.getResult().savePartition(src + ".part");
//        System.out.println("logMLE = " + LogLikelihoodFunction.loglikelihood(cec.getResult()));
//        System.out.println("logMLE = " + new LogMLEScore().score(cec.getResult()));
//        System.out.println("AIC = " + new AICScore().score(cec.getResult()));
//        System.out.println("BIC = " + new BICScore().score(cec.getResult()));
    }
}
