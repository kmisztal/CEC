package cec.cluster.types.gaussian;

import cec.cluster.types.Cost;

/**
 *
 * @author Krzysztof
 */
public class SphericalGaussians extends Cost {

    @Override
    public double h() {
        return cluster.getWeight() * (-Math.log(cluster.getWeight())
                + cluster.getDimension() * 0.5 * Math.log(2 * Math.PI * Math.E / cluster.getDimension())
                + 0.5 * cluster.getCov().trace()
                );
    }

    @Override
    public String getInfo() {
        return "Spherical Gaussians: radial Gaussian densities,";
    }

}
