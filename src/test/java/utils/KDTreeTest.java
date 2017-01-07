package utils;

import cec.cluster.Point;
import org.junit.Assert;
import org.junit.Test;
import tools.kdtree.KDTree;
import tools.kdtree.TreeNotConstructedYetException;
import tools.kdtree.WrongDimensionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KDTreeTest {
    @Test
    public void wikipediaTest() throws WrongDimensionException, TreeNotConstructedYetException {
        List<Point> points = Arrays.asList(
                new Point(1.0, 2.0, 3.0),
                new Point(1.0, 5.0, 4.0),
                new Point(1.0, 9.0, 6.0),
                new Point(1.0, 4.0, 7.0),
                new Point(1.0, 8.0, 1.0),
                new Point(1.0, 7.0, 2.0));
        KDTree tree = KDTree.create(points);
        Point p = new Point(1.0, 9.0, 2.0);
        Point expectedNearestPoint = new Point(1.0, 8.0, 1.0);
        Point nearestPoint = tree.findNearestNeighbour(p);
        Assert.assertEquals(expectedNearestPoint, nearestPoint);
    }

    @Test
    public void random1000000Test() throws WrongDimensionException, TreeNotConstructedYetException {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000000; ++i) {
            points.add(Point.createRandomPoint(1, 3, 10));
        }

        Point p = Point.createRandomPoint(1, 3, 10);

        long start = System.currentTimeMillis();
        double dist = Double.MAX_VALUE;
        Point nearestPoint = null;
        for (Point point : points) {
            double d = point.dist(p);
            if (d < dist) {
                dist = d;
                nearestPoint = point;
            }
        }
        long stop = System.currentTimeMillis();
        Point expectedResult = nearestPoint;
        System.out.printf("Ordinary way took %d miliseconds and found %s\n", (stop - start), nearestPoint.toString());

        KDTree tree = KDTree.create(points);
        start = System.currentTimeMillis();
        nearestPoint = tree.findNearestNeighbour(p);
        stop = System.currentTimeMillis();
        System.out.printf("KDTree way took %d miliseconds and found %s\n", (stop - start), nearestPoint.toString());
        Assert.assertEquals(expectedResult, nearestPoint);
    }
}
