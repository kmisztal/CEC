package cec.input.generator;

import cec.cluster.Point;
import cec.input.DataReader;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class UniformGenerator{
    private Random random = new Random();
    public double nextDouble(double begin, double end) {
        return begin + random.nextDouble() * (end - begin);
    }
}

public class GaussianGenerator extends DataReader{
    private UniformGenerator uniformGenerator = new UniformGenerator();
    private MultivariateNormalDistribution[] distribution;
    private double[][] means;
    private int dimensions;
    private int centers;
    private int pointsPerCenter;
    private Type type;

    //Range of possible centers in each dimension.
    private double rangeMin = -10.0f;
    private double rangeMax = 10.0f;

    //Range of possible values of standard deviation.
    private final double stdMin = 0.1f;
    private final double stdMax = 2.0f;


    private enum Type {
        ALL{
            public double[][][] setCovariance(int centers, int dimensions, UniformGenerator generator,
                                              double stdMin, double stdMax){
                double[][][] matrices = new double[centers][][];
                for (int index = 0; index < centers; ++index) {
                    matrices[index] = getRandomCovMatrix(dimensions, generator, stdMin, stdMax);
                }
                return matrices;
            }

            private double[][] getRandomCovMatrix(int dimensions, UniformGenerator generator,
                                                  double stdMin, double stdMax) {
                double[][] data = new double[dimensions][dimensions];
                LUDecomposition lu;
                do {
                    for (int i = 0; i < data.length; ++i) {
                        for (int j = 0; j < i; ++j) {
                            double random_1 = generator.nextDouble(stdMin, stdMax);
                            double random_2 = generator.nextDouble(stdMin, stdMax);
                            data[i][j] = data[data.length - 1 - i][data.length - 1 - j]
                                    = random_1 * random_2;
                        }
                        double random = generator.nextDouble(stdMin, stdMax);
                        data[i][i] = random * random;
                    }
                    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
                    lu = new LUDecomposition(matrix);

                } while (lu.getDeterminant() < 0.0);
                return data;
            }
        },
        SPHERICAL{
            public double[][][] setCovariance(int centers, int dimensions, UniformGenerator generator,
                                              double stdMin, double stdMax){
                double[][][] matrices = new double[centers][][];
                //Generates spheres with random radius
                for (int i = 0; i < centers; ++i) {
                    double std = generator.nextDouble(stdMin, stdMax);
                    matrices[i] = getSphereMatrix(std*std, dimensions);
                }
                return matrices;
            }
        },
        FIXEDR{
            public double[][][] setCovariance(int centers, int dimensions, UniformGenerator generator,
                                              double stdMin, double stdMax){
                double[][][] matrices = new double[centers][][];
                //Generates spheres with the same radius
                double std = generator.nextDouble(stdMin, stdMax);
                for (int i = 0; i < centers; ++i) {
                    matrices[i] = getSphereMatrix(std*std, dimensions);
                }
                return matrices;
            }
        },
        DIAGONAL{
            public double[][][] setCovariance(int centers, int dimensions, UniformGenerator generator,
                                              double stdMin, double stdMax){
                double[][][] matrices = new double[centers][][];
                //Generates random "diagonal" ellipses
                for (int i = 0; i < centers; ++i) {
                    matrices[i] = new double[dimensions][dimensions];

                    for (int j = 0; j < dimensions; ++j) {
                        double std = generator.nextDouble(stdMin, stdMax);
                        matrices[i][j][j] = std*std;
                    }
                }
                return matrices;
            }
        };

        public abstract double[][][] setCovariance(int centers, int dimensions, UniformGenerator generator,
                                                   double stdMin, double stdMax);

        protected double[][] getSphereMatrix(double variance, int dimensions) {
            double covariance[][] = new double[dimensions][dimensions];

            for (int i = 0; i < dimensions; ++i) {
                covariance[i][i] = variance;
            }

            return covariance;
        }
    }

    public GaussianGenerator(int dimensions, int centers, int pointsPerCenter) {
        this.dimensions = dimensions;
        this.centers = centers;
        this.pointsPerCenter = pointsPerCenter;

        //--- Generates random means. ---
        double[][] means = new double[centers][];
        for (int i = 0; i < centers; ++i) {
            double[] mean = new double[dimensions];
            for (int j = 0; j < dimensions; ++j) {
                mean[j] = uniformGenerator.nextDouble(rangeMin, rangeMax);
            }
            means[i] = mean;
        }
    }

    public GaussianGenerator(int dimensions, int pointsPerCenter, double[][] clusterMeanCoordinates) {
        this.dimensions = dimensions;
        this.centers = clusterMeanCoordinates.length;
        this.pointsPerCenter = pointsPerCenter;
        means = clusterMeanCoordinates;
    }

    public void setClusterMeansRange( double rangeMin, double rangeMax){
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    @Override
    public boolean type(String type) {
        return this.type.name().equals(type);
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        this.type = Type.valueOf(type);


        //--- Generates random covariance matrices ---
        double[][][] covMatrices = this.type.setCovariance(centers, dimensions, uniformGenerator,
                stdMin, stdMax);

        distribution = new MultivariateNormalDistribution[centers];

        for (int i = 0; i < centers; ++i) {
            distribution[i] = new MultivariateNormalDistribution(means[i], covMatrices[i]);
        }

        double[][] rawCoordinates = getPoints(pointsPerCenter);
        List<Point> points = new ArrayList<>();
        for(double[] pointCoordinates : rawCoordinates){
            points.add(new Point(1, pointCoordinates));
        }
        return points;
    }


    public double[][] getPoints(int n) {
        double[][] samples = new double[n][];

        int m = 0;
        int nDist = distribution.length;
        for (int i = 0; i < n; ++i) {
            samples[i] = distribution[m%nDist].sample();
            ++m;
        }
        return samples;
    }
}

