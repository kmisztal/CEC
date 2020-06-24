package cec.test;

import cec.CEC;
import cec.cluster.types.ClusterKind;
import cec.input.draw.DataDraw;
import stats.AICScore;
import stats.BICScore;
import stats.LogMLEScore;

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
    public static void main(String[] args) throws IOException {

        CEC cec = new CEC();

        // to pass command line arguments
        // these arguments overwrite other default/file configuration
        //CEC cec = new CEC(args);

        String src = "src/main/resources/data_test/sets/spirala.txt";
        cec.setData(src,
                "text/space-separated-values");

        cec.add(ClusterKind.Gaussians, 30);
//
//        cec.add(ClusterKind.LambdaGaussians, 3,
//                TypeOption.add("lambda", new double[]{1., 0.1}));
//
//        cec.add(ClusterKind.CovarianceGaussians, 3,
//                TypeOption.add("covariance", new double[][]{{1., 0.1}, {0.1, 1}}));
//
//        cec.add(ClusterKind.DeterminantGaussians, 4,
//                TypeOption.add("det", 0.1)
//        );
//        
//        cec.add(ClusterKind.DiagonalGaussians, 3);
        
//        cec.add(ClusterKind.SphericalGaussians, 30);
        
//        cec.add(ClusterKind.SphericalGaussiansWithFixedRadius, 3,
//                TypeOption.add("r", 0.5)
//        );

        cec.run();

        //print the results
        //and if it possible you will see the plot
        cec.showResults();

        //save results to file
        cec.saveResults();

        new DataDraw(cec.getResult()).disp();

        //        System.out.println(cec.getResult().getPartition());
        cec.getResult().savePartition(src + ".part");


        //        System.out.println("logMLE = " + LogLikelihoodFunction.loglikelihood(cec.getResult()));
        System.out.println("logMLE = " + new LogMLEScore().score(cec.getResult()));
        System.out.println("AIC = " + new AICScore().score(cec.getResult()));
        System.out.println("BIC = " + new BICScore().score(cec.getResult()));
    }

}
