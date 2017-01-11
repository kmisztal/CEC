package tools.kdtree;

import cec.cluster.Point;
import tools.kdtree.exceptions.NoChildFoundException;
import tools.kdtree.exceptions.TreeNotConstructedYetException;
import tools.kdtree.exceptions.WrongDimensionException;

import java.util.List;

/**
 * Created by pawel on 10.01.17.
 */
public interface KDTree {
    Point findNearestNeighbour(Point p) throws TreeNotConstructedYetException, WrongDimensionException, NoChildFoundException;
    List<List<Point>> getClusters(int depth);
}
