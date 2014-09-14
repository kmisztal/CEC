package cec.input.draw;

import cec.cluster.Cluster;
import de.erichseifert.gral.data.DataTable;
import static java.lang.Math.sqrt;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class Ellipse {

    public double x0, y0, a, b, theta;

    public Ellipse(Cluster c) {
        final SimpleMatrix cov = c.getCov();
        double[][] v = eig(cov, true);
        a = 2. * Math.sqrt(v[1][0]);
        b = 2. * Math.sqrt(v[0][0]);
        x0 = c.getMean().get(0, 0);
        y0 = c.getMean().get(1, 0);

        v = eig(cov, false);
        theta = Math.atan2(v[0][1], v[0][0]) + Math.PI / 2.;
        if (Double.isNaN(theta)) {
            theta = 0.;
        }
    }

    private double[][] eig(SimpleMatrix cov, boolean eigenvalues) {
        if (cov.numRows() == cov.numCols() && cov.numCols() == 2) {
            final double a = cov.get(0, 0),
                    b = cov.get(0, 1),
                    c = cov.get(1, 0),
                    d = cov.get(1, 1);
            if (eigenvalues) {
                return new double[][]{
                    {(a + d - sqrt(a * a + 4 * b * c - 2 * a * d + d * d)) / 2.},
                    {(a + d + sqrt(a * a + 4 * b * c - 2 * a * d + d * d)) / 2.}
                };
            } else {
                return new double[][]{
                    {-(-a + d + sqrt(a * a + 4 * b * c - 2 * a * d + d * d)) / (2. * c), 1},
                    {-(-a + d - sqrt(a * a + 4 * b * c - 2 * a * d + d * d)) / (2. * c), 1}
                };
            }
        } else {
            throw new RuntimeException("Not supported yet: matrix should be 2x2");
        }
    }

    public void addData(DataTable d) {
        final double step = Math.PI/180.;
        for(double i = 0; i < 2*Math.PI; i+=step){
            d.add(x0 + a * Math.cos(i) * Math.cos(theta) - b * Math.sin(i) * Math.sin(theta),
                    y0 + a * Math.cos(i) * Math.sin(theta) + b * Math.sin(i) * Math.cos(theta));
        }
    }

}
