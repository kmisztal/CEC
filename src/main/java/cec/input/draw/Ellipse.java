package cec.input.draw;

import cec.cluster.Cluster;
import de.erichseifert.gral.data.DataTable;
import org.ejml.simple.SimpleMatrix;

import static java.lang.Math.sqrt;

/**
 * @author Krzysztof
 */
public class Ellipse {

    public final double x0;
    public final double y0;
    public final double a;
    public final double b;
    public double theta;

    public Ellipse(Cluster c) {
        final SimpleMatrix cov = c.getCostFunction().getCov();
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
            final double a = cov.get(0, 0);
            final double b = cov.get(0, 1);
            final double c = cov.get(1, 0);
            final double d = cov.get(1, 1);
            double sqrt = sqrt(a * a + 4 * b * c - 2 * a * d + d * d);
            if (eigenvalues) {
                return new double[][]{
                        {(a + d - sqrt) / 2.},
                        {(a + d + sqrt) / 2.}
                };
            } else {
                return new double[][]{
                        {-(-a + d + sqrt) / (2. * c), 1},
                        {-(-a + d - sqrt) / (2. * c), 1}
                };
            }
        } else {
            throw new RuntimeException("Not supported yet: matrix should be 2x2");
        }
    }

    public void addData(DataTable d) {
        final double step = Math.PI / 180.;
        for (double i = 0; i < 2 * Math.PI; i += step) {
            d.add(x0 + a * Math.cos(i) * Math.cos(theta) -  b *  Math.sin(i) * Math.sin(theta),
                    y0 + a * Math.cos(i) * Math.sin(theta) + b * Math.sin(i) * Math.cos(theta));
        }
    }

}
