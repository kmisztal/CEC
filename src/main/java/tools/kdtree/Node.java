package tools.kdtree;

import cec.cluster.Point;

public class Node {
    private Point value;
    private int divisionCoordinate;
    private double divisionValue;
    private Node leftTree;
    private Node rightTree;

    public Node(Point value, int divisionCoordinate, double divisionValue, Node leftTree, Node rightTree) {
        this.value = value;
        this.divisionCoordinate = divisionCoordinate;
        this.divisionValue = divisionValue;
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    public Point getValue() {
        return value;
    }

    public int getDivisionCoordinate() {
        return divisionCoordinate;
    }

    public double getDivisionValue() {
        return divisionValue;
    }

    public Node getLeftTree() {
        return leftTree;
    }

    public Node getRightTree() {
        return rightTree;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", divisionCoordinate=" + divisionCoordinate +
                ", divisionValue=" + divisionValue +
                ", leftTree=" + leftTree +
                ", rightTree=" + rightTree +
                '}';
    }
}
