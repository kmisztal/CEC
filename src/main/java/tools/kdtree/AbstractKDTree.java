package tools.kdtree;

import cec.cluster.Point;
import tools.kdtree.exceptions.NoChildFoundException;
import tools.kdtree.exceptions.TreeNotConstructedYetException;
import tools.kdtree.exceptions.WrongDimensionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 06.01.17.
 *
 * Implementation was created basing on sources:
 * https://en.wikipedia.org/wiki/K-d_tree
 * http://rosettacode.org/wiki/K-d_tree
 *
 */
public abstract class AbstractKDTree implements KDTree {
    protected Node root;
    protected int dimension;
    protected List<Point> points;

    public AbstractKDTree(List<Point> input_points) {
        this.points = new ArrayList<>(input_points);
        this.dimension = points.get(0).getDimension();
        this.root = create(points, 0);
    }

    abstract protected int getDivisionCoordinate(List<Point> points, int depth);

    abstract protected List<Integer> getDivisionIndecies(List<Point> points, int divisionCoordinate, int depth);

    private Node create(List<Point> points, int depth) {
        if (points.size() == 0) {
            return null;
        }

        int coordinate = getDivisionCoordinate(points, depth);

        if (points.size() == 1) {
            return new Node(
                    Arrays.asList(points.get(0)),
                    coordinate,
                    new ArrayList<Double>(),
                    new ArrayList<Node>());
        }

        points.sort(new PointComparator(coordinate, dimension));
        List<Integer> divisionIndecies = getDivisionIndecies(points, coordinate, depth);
        List<Point> divisionPoints = divisionIndecies.stream().map(i->points.get(i)).collect(Collectors.toList());
        List<Double> divisionValues = divisionPoints.stream().map(p -> p.get(coordinate)).collect(Collectors.toList());

        List<Node> children = new ArrayList<>();
        children.add(create(points.subList(0, divisionIndecies.get(0)), depth + 1));
        for (int i = 0; i < divisionPoints.size() - 1; ++i) {
            children.add(create(points.subList(divisionIndecies.get(i), divisionIndecies.get(i+1)), depth + 1));
        }
        children.add(create(points.subList(divisionIndecies.get(divisionPoints.size() - 1) + 1, points.size()), depth + 1));

        return new Node(divisionPoints, coordinate, divisionValues, children);
    }

    public Point findNearestNeighbour(Point p) throws TreeNotConstructedYetException, WrongDimensionException, NoChildFoundException {
        if (root == null) {
            throw new TreeNotConstructedYetException("Construct the tree first");
        }
        if (dimension != p.getDimension()) {
            throw new WrongDimensionException("Dimension of the given point does not fit to dimension of points in the tree");
        }
        return nn(root, p);
    }

    private Point nearestToPointFromList(Point p, List<Point> points) throws NoChildFoundException {
        Point result = null;
        double dist = Double.MAX_VALUE;
        for (Point point : points) {
            if (point.dist(p) < dist) {
                result = point;
                dist = point.dist(p);
            }
        }
        if (result == null) {
            throw new NoChildFoundException();
        }
        return result;
    }

    private Point nn(Node root, Point p) throws NoChildFoundException {
        if (root.getChildren() == null || root.getChildren().size() == 0) {
            double tempDist = Double.MAX_VALUE;
            Point tempPoint = null;
            for (Point point : root.getValues()) {
                if (point.dist(p) < tempDist) {
                    tempDist = point.dist(p);
                    tempPoint = point;
                }
            }
            return tempPoint;
        }

        final int coordinate = root.getDivisionCoordinate();

        int childIndex = root.getChildIndexFor(p);
        Node childToGo = root.getChildren().get(childIndex);

        Point tempNearestPoint = (childToGo == null ? null : nn(childToGo, p));
        double tempBestDistance = (tempNearestPoint == null ? Double.MAX_VALUE : tempNearestPoint.dist(p));

        Point nearestPointFromRoot = nearestToPointFromList(p, root.getValues());
        double distToNearestPointFromRoot = nearestPointFromRoot.dist(p);

        if (distToNearestPointFromRoot < tempBestDistance) {
            tempNearestPoint = nearestPointFromRoot;
            tempBestDistance = distToNearestPointFromRoot;
        }

        int leftHiperplaneIndex = root.getLeftHiperPlaneIndexFor(p);
        Point leftHiperplane = (leftHiperplaneIndex <= -1 ? null :root.getValues().get(leftHiperplaneIndex));

        int rightHiperplaneIndex = root.getRightHiperPlaneIndexFor(p);
        Point rightHiperplane = (rightHiperplaneIndex >= root.getValues().size() ? null : root.getValues().get(rightHiperplaneIndex));

        double leftRootDistance = (leftHiperplane == null ? Double.MAX_VALUE : leftHiperplane.dist(p));
        double rightRootDistance = (rightHiperplane == null ? Double.MAX_VALUE : rightHiperplane.dist(p));

        double leftHiperplaneDistance = (leftHiperplane == null ? Double.MAX_VALUE : Math.abs(p.get(coordinate) - leftHiperplane.get(coordinate)));
        double rightHiperplaneDistance = (rightHiperplane == null ? Double.MAX_VALUE : Math.abs(p.get(coordinate) - rightHiperplane.get(coordinate)));



        if (leftRootDistance < tempBestDistance) {
            tempBestDistance = leftRootDistance;
            tempNearestPoint = leftHiperplane;
        }

        if (rightRootDistance < tempBestDistance) {
            tempBestDistance = rightRootDistance;
            tempNearestPoint = rightHiperplane;
        }

        if (root.getChildren() != null && tempBestDistance > rightHiperplaneDistance && childIndex + 1 < root.getChildren().size() && root.getChildren().get(childIndex + 1) != null) {
            Point nearestRightPoint = nn(root.getChildren().get(childIndex + 1), p);
            if (nearestRightPoint.dist(p) < tempBestDistance) {
                tempBestDistance = nearestRightPoint.dist(p);
                tempNearestPoint = nearestRightPoint;
            }
        }

        if (root.getChildren() != null && tempBestDistance > leftHiperplaneDistance && childIndex - 1 >= 0 && root.getChildren().get(childIndex - 1) != null) {
            Point nearestLeftPoint = nn(root.getChildren().get(childIndex - 1), p);
            if (nearestLeftPoint.dist(p) < tempBestDistance) {
                tempBestDistance = nearestLeftPoint.dist(p);
                tempNearestPoint = nearestLeftPoint;
            }
        }

        return tempNearestPoint;
    }

    public List<List<Point>> getClusters(int depth) {
        return getCluster(root, 0, depth);
    }

    private List<List<Point>> getCluster(Node root, int actualDepth, int maxDepth) {
        if (actualDepth == maxDepth) {
            List<List<Point>> result = new ArrayList<List<Point>>();
            for (Node child : root.getChildren()) {
                result.add(getPoints(child));
            }
            return result;
        }
        List<List<Point>> result = new ArrayList<List<Point>>();
        for (Node child : root.getChildren()) {
            result.addAll(getCluster(child, actualDepth + 1, maxDepth));
        }
        return result;
    }

    private List<Point> getPoints(Node root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Point> result = new ArrayList<>();
        result.addAll(root.getValues());
        for (Node child : root.getChildren()) {
            result.addAll(getPoints(child));
        }
        return result;
    }

    @Override
    public String toString() {
        return "AbstractKDTree{" +
                "root=" + root +
                '}';
    }
}
