package cec.cluster;

import org.ejml.simple.SimpleMatrix;
import tools.NumberUtils;

/**
 * @author Krzysztof
 */
public class Point implements ClusterLike, Comparable<Point> {

    private static final double epsilon = 1.;
    private final static double delta = 0.01;
    private static SimpleMatrix cov = null;
    private final SimpleMatrix x;
    private final double weight;
//    private int partition = -1;

    public Point(double weight, double... x) {
        final int dim = x.length;
        this.weight = weight;
        this.x = new SimpleMatrix(dim, 1, true, x);
        synchronized (Point.class) {
            if (cov == null) {
                Point.cov = new SimpleMatrix(dim, dim);

                for (int i = 0; i < dim; ++i) {
                    Point.cov.set(i, i, Math.pow(delta, dim + 2.) * 1.0 / 12.0 * epsilon);//TODO jak to wyglada w wyzszych wymiarach
                }
            }
        }
    }

    public Point(double weight, int dimension) {
        this.x = new SimpleMatrix(dimension, 1);
        this.weight = weight;
        synchronized (Point.class) {
            if (cov == null || Point.cov.numRows() != dimension) {
                Point.cov = new SimpleMatrix(dimension, dimension);

                for (int i = 0; i < dimension; ++i) {
                    Point.cov.set(i, i, Math.pow(delta, dimension + 2.) * 1.0 / 12.0 * epsilon);//TODO jak to wyglada w wyzszych wymiarach
                }
            }
        }
    }

    //TODO: this method can perform really badly please look out
    public Point(double weight, String[] ls) {
        this(weight, ls.length);

        for (int i = 0; i < ls.length; ++i) {
            try {
                this.x.set(i, 0, NumberUtils.createNumber(ls[i]).doubleValue());
            } catch (NumberFormatException ignored) {
                this.x.set(i, 0, NumberUtils.createNumber(ls[i].trim().replaceAll("[^0-9.+-eE]", "")).doubleValue());
            }
        }
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public SimpleMatrix getMean() {
        return x;
    }

    @Override
    public SimpleMatrix getCov() {
        return cov;
    }

    @Override
    public int getCardinality() {
        return 1;
    }

    @Override
    public int compareTo(Point t) {
        final int dim = getDimension();
        if (dim != t.getDimension()) {
            return 1;
        }
        for (int i = 0; i < dim; ++i) {
            if (get(i) != t.get(i)) {
                return -1;
            }
        }
        return 0;
    }

    public boolean equals(Object t) {
        return t instanceof Point && compareTo((Point) t) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = x != null ? x.hashCode() : 0;
        temp = Double.doubleToLongBits(getWeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int getDimension() {
        return x.numRows();
    }

    public double get(int i) {
        return x.get(i, 0);
    }

//    public int getPartition() {
//        return partition;
//    }
//
//    public void setPartition(int partition) {
//        this.partition = partition;
//    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < x.numRows(); ++i) {
            ret.append(x.get(i, 0));
            ret.append(" ");
        }
        return ret.toString();
    }

    public double dist(Point p) {
        if (this.getDimension() != p.getDimension())
            throw new RuntimeException("Distance of points from different dimensions");
        double ret = 0;
        for (int i = 0; i < getDimension(); ++i) {
            ret += Math.pow(get(i) - p.get(i), 2.0);
        }
        return Math.sqrt(ret);
    }
}
