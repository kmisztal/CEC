/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cec.test;

import cec.CEC;
import cec.cluster.types.ClusterKind;
import cec.cluster.types.TypeOption;
import cec.input.draw.DataDraw;

import java.io.IOException;

/**
 *
 * @author Krzysztof
 */
public class Test3 {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws IOException {

        CEC cec = new CEC();
        cec.setData("datat/wyk/15p.txt",
                "text/space-separated-values");

//        cec.add(ClusterKind.Gaussians, 1);
        
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
        
//        cec.add(ClusterKind.SphericalGaussians, 1);
        
//        cec.add(ClusterKind.SphericalGaussiansWithFixedRadius, 3,
//                TypeOption.add("r", 0.5)
//        );

        //r = 254,065
        
        /*
        3078,679  -2309,792  
        -2309,792  1745,759 
        */
        
        //4816.24, 8.19986
        
        cec.add(ClusterKind.LambdaGaussians, 7,
                TypeOption.add("lambda", new double[]{4816.24, 8.19986}));
        cec.add(ClusterKind.SphericalGaussiansWithFixedRadius, 2,
                TypeOption.add("r", 254.065)
        );
        
        cec.run();

        //print the results
        //and if it possible you will see the plot
        cec.showResults();

        //save results to file
        cec.saveResults();

        new DataDraw(cec.getResult()).disp();        
    }
}
