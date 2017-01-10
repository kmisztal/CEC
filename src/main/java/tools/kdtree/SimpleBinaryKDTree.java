package tools.kdtree;

import cec.cluster.Point;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pawel on 09.01.17.
 */
public class SimpleBinaryKDTree extends AbstractKDTree {
    public SimpleBinaryKDTree(List<Point> points) throws WrongDimensionException {
        super(points);
    }

    @Override
    protected int getDivisionCoordinate(List<Point> points, int depth) {
        return depth % dimension;
    }

    @Override
    protected List<Integer> getDivisionIndecies(List<Point> points, int divisionCoordinate, int depth) {
        return Arrays.asList((points.size() % 2 == 0 ? points.size() / 2 : (points.size() + 1) / 2));
    }
}
