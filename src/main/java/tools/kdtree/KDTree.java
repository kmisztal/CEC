package tools.kdtree;

import cec.cluster.Point;

import java.util.List;

/**
 * Created by pawel on 06.01.17.
 *
 * Implementation was created basing on sources:
 * https://en.wikipedia.org/wiki/K-d_tree
 * http://rosettacode.org/wiki/K-d_tree
 *
 */
public class KDTree {
    private Node root;
    private int dimension;

    private KDTree(Node root) {
        this.root = root;
        this.dimension = root.getValue().getDimension();
    }

    public static KDTree create(List<Point> points) throws WrongDimensionException {
        return new KDTree(create(points, 0));
    }

    private static Node create(List<Point> points, int depth) throws WrongDimensionException {
        if (points.size() == 0) {
            return null;
        }

        final int dimension = points.get(0).getDimension();
        final int coordinate = depth % dimension;

        if (points.size() == 1) {
            return new Node(points.get(0), coordinate, points.get(0).get(coordinate), null, null);
        }

        if (!points.stream().allMatch(p-> p.getDimension() == dimension)) {
            throw new WrongDimensionException("At least one point has inproper dimension");
        }

        points.sort(new PointComparator(coordinate, dimension));
        // W kodzie z wikipedii nie ma rozróżnienia na parzystą czy nieparzystą ilość punktów
        // stąd różnica w wynikach
        // Żeby jeszcze przyspieszyć - zaimplementować medianę-median - liniowo znajduje medianę
        // tutaj n log(n) zajmuje sortowanie
//        int medianIndex = (points.size() % 2 == 0 ? points.size() / 2 : (points.size() + 1)  / 2);
        int medianIndex = points.size() / 2;
        Point median = points.get(medianIndex);

        return new Node(median,
                coordinate,
                median.get(coordinate),
                create(points.subList(0, medianIndex), depth + 1),
                create(points.subList(medianIndex + 1, points.size()), depth + 1)
        );

    }

    public Point findNearestNeighbour(Point p) throws TreeNotConstructedYetException, WrongDimensionException {
        if (root == null) {
            throw new TreeNotConstructedYetException("Construct the tree first");
        }
        if (root.getValue().getDimension() != p.getDimension()) {
            throw new WrongDimensionException("Dimension of the given point does not fit to dimension of points in the tree");
        }
        return nn(root, p).getValue();
    }


    private Node nn(Node root, Point p) {
        if (root.getLeftTree() == null && root.getRightTree() == null) {
            return root;
        }

        final int coordinate = root.getDivisionCoordinate();
        PointComparator comparator = new PointComparator(coordinate, root.getValue().getDimension());

        if (comparator.compare(root.getValue(), p) == 0) {
            return root;
        }

        Node tempNearest;
        boolean wentLeft;
        if (comparator.compare(root.getValue(), p) < 0) {
            if (root.getRightTree() == null) return root;
            tempNearest = nn(root.getRightTree(), p);
            wentLeft = false;
        } else {
            if (root.getLeftTree() == null) return root;
            tempNearest = nn(root.getLeftTree(), p);
            wentLeft = true;
        }

        double tempBestDistance = tempNearest.getValue().dist(p);
        double rootDistance = root.getValue().dist(p);

        if (rootDistance < tempBestDistance) {
            tempNearest = root;
            tempBestDistance = rootDistance;
        }

        double distanceToPlane = Math.pow(Math.abs(root.getDivisionValue() - tempNearest.getValue().get(root.getDivisionCoordinate())), 2);
        if (tempBestDistance < distanceToPlane) {
            Node nearestInTheOtherSideOfPlane;
            if (wentLeft) {
                if (root.getRightTree() == null) return tempNearest;
                nearestInTheOtherSideOfPlane = nn(root.getRightTree(), p);
            } else {
                if (root.getLeftTree() == null) return tempNearest;
                nearestInTheOtherSideOfPlane = nn(root.getLeftTree(), p);
            }
            double distToNearestInTheOtherSideOfPlane = nearestInTheOtherSideOfPlane.getValue().dist(p);
            if (distToNearestInTheOtherSideOfPlane < tempBestDistance) {
                tempNearest = nearestInTheOtherSideOfPlane;
            }
        }

        return tempNearest;
    }

    @Override
    public String toString() {
        return "KDTree{" +
                "root=" + root +
                '}';
    }
}
