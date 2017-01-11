package utils;

import cec.cluster.Point;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tools.kdtree.*;
import tools.kdtree.exceptions.NoChildFoundException;
import tools.kdtree.exceptions.TreeNotConstructedYetException;
import tools.kdtree.exceptions.WrongDimensionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class SimpleBinaryKDTreeRandomTest {

    @Parameterized.Parameter(value = 0)
    public int setSize;

    @Parameterized.Parameter(value = 1)
    public int trials;

    @Parameterized.Parameters(name = "{index}: setSize = {0}, trials = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {10,10}, {100, 100}, {1000, 1000}, {10000, 10000}, {20000, 20000}
        });
    }

    @Test
    public void randomParametrizedTest() throws WrongDimensionException, TreeNotConstructedYetException, NoChildFoundException {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < setSize; ++i) {
            points.add(Point.createRandomPoint(1, 3, 10));
        }
        KDTree tree = KDTreeFactory.createSimpleBinaryKDTree(points);

        for (int i = 0; i < trials; i++) {

            Point p = Point.createRandomPoint(1, 3, 10);

            double dist = Double.MAX_VALUE;
            Point nearestPoint = null;
            for (Point point : points) {
                double d = point.dist(p);
                if (d < dist) {
                    dist = d;
                    nearestPoint = point;
                }
            }
            Point expectedResult = nearestPoint;


            nearestPoint = tree.findNearestNeighbour(p);
            Assert.assertEquals(expectedResult, nearestPoint);
        }
    }




}
