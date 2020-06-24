package cec.input.generator;

import cec.cluster.Point;
import cec.input.DataReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MouseGenerator extends DataReader {
    private class Hyperball {
        public int dimension;
        public double[] center;
        public double radius;
        public Hyperball(int dimension, double[] center, double radius) {
            this.dimension = dimension;
            this.center = center;
            this.radius = radius;
        }

        public boolean isInside(double[] point) {
            double sum = 0;
            for (int i = 0; i < point.length; ++i) {
                sum += (center[i] - point[i])*(center[i] - point[i]);
            }
            double distance = Math.sqrt(sum);

            return distance <= radius;
        }
    }

    private class Interval {
        public double left;
        public double right;

        public Interval(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    private Hyperball head, leftEar, rightEar;
    private Interval[] hiperrectangle;
    private int points;
    private int dimension;

    private Point getPoint() {
        double[] coordinates;
        UniformGenerator unifGenerator = new UniformGenerator();
        boolean isInside = false;
        do {
            coordinates = new double[dimension];
            for (int j = 0; j < dimension; ++j) {
                coordinates[j] = unifGenerator.nextDouble(hiperrectangle[j].left, hiperrectangle[j].right);
            }
            if (head.isInside(coordinates)) {
                isInside = true;
            }
            else if (leftEar.isInside(coordinates)) {
                isInside = true;
            }
            else if (rightEar.isInside(coordinates)) {
                isInside = true;
            }
        } while (!isInside);


        return new Point(1, coordinates);
    }

    //dimension is at least equal 2
    public MouseGenerator(int dimension, int points) {
        this.points = points;
        this.dimension = dimension;
        double[] headCenter = new double[dimension];
        double[] leftEarCenter = new double[dimension];
        double[] rightEarCenter = new double[dimension];
        double headRadius = 4.0;
        double earRadius = 2.0;

        leftEarCenter[0] = -((headRadius + earRadius) / Math.sqrt(2));
        leftEarCenter[1] = (headRadius + earRadius) / Math.sqrt(2);
        rightEarCenter[0] = (headRadius + earRadius) / Math.sqrt(2);
        rightEarCenter[1] = (headRadius + earRadius) / Math.sqrt(2);

        head = new Hyperball(dimension, headCenter, headRadius);
        leftEar = new Hyperball(dimension, leftEarCenter, earRadius);
        rightEar = new Hyperball(dimension, rightEarCenter, earRadius);

        hiperrectangle = new Interval[dimension];
        hiperrectangle[0] = new Interval(leftEarCenter[0] - earRadius, rightEarCenter[0] + earRadius);
        hiperrectangle[1] = new Interval(headCenter[1] - headRadius, leftEarCenter[1] + earRadius);

        for (int i = 2; i < dimension; ++i) {
            hiperrectangle[i] = new Interval(headCenter[i] - headRadius, headCenter[i] + headRadius);
        }
    }

    @Override
    public boolean type(String type) {
        return false;
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        Point[] points = new Point[this.points];
        for (int i = 0; i < points.length; ++i) {
            points[i] = getPoint();
        }

        return Arrays.asList(points);
    }
}
