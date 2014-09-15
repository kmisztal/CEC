package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;

/**
 *
 * @author Krzysztof
 */
public class SphericalGaussiansWithFixedRadius extends Cost {

    @Override
    public double h() {
        return cluster.getWeight() 
                * (
                    - Math.log(cluster.getWeight())
                    + cluster.getDimension() * 0.5 * Math.log(2. * Math.PI * Math.E)
                    + 0.5 * Math.log(cluster.getCov().determinant() * (cluster.getCardinality() - 1.) / cluster.getCardinality())
                );
    }

    @Override
    public String getInfo() {
        return "Gaussian: All Gaussian distributions";
    }

}