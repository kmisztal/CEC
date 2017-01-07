package tools.kdtree;

import cec.cluster.Point;

import java.util.Comparator;

public class PointComparator implements Comparator<Point> {
    private int coordinate;
    private int dimension;

    public PointComparator(int coordinate, int dimension) {
        this.coordinate = coordinate;
        this.dimension = dimension;
    }

    @Override
    public int compare(Point o1, Point o2) {
        for (int i = coordinate; i  < dimension; ++i) {
            int tempRes = Double.compare(o1.get(i), o2.get(i));
            if (tempRes != 0) {
                return tempRes;
            }
        }
        return 0;
    }
}
