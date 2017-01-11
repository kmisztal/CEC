package tools.kdtree;

import cec.cluster.Point;
import tools.kdtree.exceptions.NoChildFoundException;
import tools.kdtree.exceptions.TreeNotConstructedYetException;
import tools.kdtree.exceptions.WrongDimensionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 10.01.17.
 */
public abstract class AbstractClusteringTree implements KDTree {

    protected Node root;
    protected int dimension;
    protected List<Point> points;

    public AbstractClusteringTree(List<Point> inputPoints) throws WrongDimensionException, IOException {
        this.points = new ArrayList<>(inputPoints);
        this.dimension = points.get(0).getDimension();
        this.root = create(points, 0);
    }

    private Node create(List<Point> points, int depth) throws IOException {

        if (points.size() == 0) {
            return null;
        }

        int coordinate = getDivisionCoordinate(points, depth);

        points.sort(new PointComparator(coordinate, dimension));
        List<Double> divisionValues = getDivisionValues(points, coordinate, depth);

        System.out.printf("Jestem na głębokości %d, mam %d punktów, dzielę się w %d punktach\n", depth, points.size(), divisionValues.size());

        List<Node> children = new ArrayList<>();
        if (divisionValues.size() == 0) {
            return new Node(points, coordinate, new ArrayList<>(), new ArrayList<>());
        }

        List<Point> tempPointsList = new ArrayList<>();
        for (Point p : points) {
            if (p.get(coordinate) < divisionValues.get(0)) {
                tempPointsList.add(p);
            }
        }
        System.out.printf("Do pierwszego koszyczka frafia %d elementów, prawy punkt podziału: %f\n", tempPointsList.size(), divisionValues.get(0));
        children.add(create(tempPointsList, depth + 1));
        int usedPoints = tempPointsList.size();

        for (int i = 0; i < divisionValues.size() - 1; ++i) {
            tempPointsList = new ArrayList<>();
            for (Point p : points) {
                if (p.get(coordinate) >= divisionValues.get(i) && p.get(coordinate) < divisionValues.get(i+1)) {
                    tempPointsList.add(p);
                }
            }
            System.out.printf("Do %d-ego koszyczka frafia %d elementów\n", i,tempPointsList.size());
            children.add(create(tempPointsList, depth + 1));
            usedPoints += tempPointsList.size();
        }

        tempPointsList = new ArrayList<>();
        for (Point p : points) {
            if (p.get(coordinate) >= divisionValues.get(0)) {
                tempPointsList.add(p);
            }
        }
        System.out.printf("Do ostatniego koszyczka frafia %d elementów, lewy punkt podziału: %f\n", tempPointsList.size(), divisionValues.get(divisionValues.size()-1));
        children.add(create(tempPointsList, depth + 1));
        usedPoints += tempPointsList.size();
        System.out.printf("Wykorzystałem %d/%d punktów\n", usedPoints, points.size());
        return new Node(coordinate, divisionValues, children);
    }

    abstract protected int getDivisionCoordinate(List<Point> points, int depth);

    abstract protected List<Double> getDivisionValues(List<Point> points, int divisionCoordinate, int depth) throws IOException;

    @Override
    public Point findNearestNeighbour(Point p) throws TreeNotConstructedYetException, WrongDimensionException, NoChildFoundException {
        return null;
    }

    @Override
    public List<List<Point>> getClusters(int depth) {
        List<List<Point>> result = getCluster(root, 0, depth);
        System.out.println("Znalazłem " + result.size() + " clastrów");
//        for (List<Point> l : result) {
//            System.out.println(l.size());
//        }
        return result.stream().filter(l->l.size() > 0).collect(Collectors.toList());
    }

    private List<List<Point>> getCluster(Node root, int actualDepth, int maxDepth) {
        if (actualDepth == maxDepth) {
            List<List<Point>> result = new ArrayList<List<Point>>();
            result.add(getPoints(root));
            return result;
        }
        List<List<Point>> result = new ArrayList<List<Point>>();
        if (root != null && root.getChildren() != null) {
            for (Node child : root.getChildren()) {
                if (child != null) {
                    result.addAll(getCluster(child, actualDepth + 1, maxDepth));
                }
            }
        }
        return result;
    }

    private List<Point> getPoints(Node root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Point> result = new ArrayList<>();
        if (root.getValues() != null) {
            result.addAll(root.getValues());
        }
        for (Node child : root.getChildren()) {
            result.addAll(getPoints(child));
        }
        return result;
    }
}
