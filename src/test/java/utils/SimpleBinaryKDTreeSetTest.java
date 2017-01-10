package utils;

import cec.CEC;
import cec.cluster.Point;
import cec.input.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tools.kdtree.*;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by pawel on 10.01.17.
 */
@RunWith(value = Parameterized.class)
public class SimpleBinaryKDTreeSetTest {
    @Parameterized.Parameter(value = 0)
    public String src;

    @Parameterized.Parameters(name = "{index}: setSize = {0}, trials = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src/main/resources/datat/mouse_1/input.txt"},
                {"src/main/resources/datat/mouse_fix_r_1/input.txt"},
                {"src/main/resources/datat/mouse_fix_r_2/input.txt"},
                {"src/main/resources/datat/mouse_fix_r_3/input.txt"}
        });
    }

    @Test
    public void givenSetTest() throws WrongDimensionException, TreeNotConstructedYetException, NoChildFoundException, InterruptedException {
        CEC cec = new CEC();
        cec.setData(src, "text/space-separated-values");
        Data data = cec.getData();
        AbstractKDTree tree = new SimpleBinaryKDTree(data.getData());

        for (int i = 0; i < 10000; ++i) {
            Point p = Point.createRandomPoint(1, 2, 10);

            double dist = Double.MAX_VALUE;
            Point nearestPoint = null;
            for (Point point : data.getData()) {
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
