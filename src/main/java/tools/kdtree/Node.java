package tools.kdtree;

import cec.cluster.Point;
import tools.kdtree.exceptions.NoChildFoundException;

import java.util.List;

public class Node {
    private List<Point> values;
    private int divisionCoordinate;
    private List<Double> divisionValues;
    private List<Node> children;

    public Node(int divisionCoordinate, List<Double> divisionValues, List<Node> children) {
        this(null, divisionCoordinate, divisionValues, children);
    }

    public Node(List<Point> values, int divisionCoordinate, List<Double> divisionValues, List<Node> children) {
        this.values = values;
        this.divisionCoordinate = divisionCoordinate;
        this.divisionValues = divisionValues;
        this.children = children;
    }

    public List<Point> getValues() {
        return values;
    }

    public int getDivisionCoordinate() {
        return divisionCoordinate;
    }

    public List<Double> getDivisionValues() {
        return divisionValues;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getChildIndexFor(Point p) throws NoChildFoundException {
        if (p.get(divisionCoordinate) < divisionValues.get(0)) {
            return 0;
        }
        if (p.get(divisionCoordinate) >= divisionValues.get(divisionValues.size() - 1)) {
            return children.size() - 1;
        }
        for (int i = 0; i < divisionValues.size() - 1; ++i) {
            if (p.get(divisionCoordinate) >= divisionValues.get(i) && p.get(divisionCoordinate) < divisionValues.get(i+1)) {
                return i+1;
            }
        }
        throw new NoChildFoundException();
    }

    public int getLeftHiperPlaneIndexFor(Point p) throws NoChildFoundException {
        if (p.get(divisionCoordinate) < divisionValues.get(0)) {
            return -1;
        }
        if (p.get(divisionCoordinate) > divisionValues.get(divisionValues.size() - 1)) {
            return divisionValues.size() - 1;
        }
        for (int i = 0; i < divisionValues.size() - 1; ++i) {
            if (p.get(divisionCoordinate) > divisionValues.get(i) && p.get(divisionCoordinate) < divisionValues.get(i+1)) {
                return i;
            }
        }
        throw new NoChildFoundException();
    }

    public int getRightHiperPlaneIndexFor(Point p) throws NoChildFoundException {
        if (p.get(divisionCoordinate) < divisionValues.get(0)) {
            return 0;
        }
        if (p.get(divisionCoordinate) > divisionValues.get(divisionValues.size() - 1)) {
            return divisionValues.size();
        }
        for (int i = 0; i < divisionValues.size() - 1; ++i) {
            if (p.get(divisionCoordinate) > divisionValues.get(i) && p.get(divisionCoordinate) < divisionValues.get(i+1)) {
                return i + 1;
            }
        }
        throw new NoChildFoundException();
    }

    public String toString() {
        return this.divisionValues.toString();
    }
}
