package utils;

import cec.CEC;
import cec.cluster.Point;
import cec.input.Data;
import cec.input.draw.Imagek;
import org.junit.Test;
import tools.kdtree.AbstractKDTree;
import tools.kdtree.SimpleBinaryKDTree;
import tools.kdtree.WrongDimensionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pawel on 09.01.17.
 */
public class KDTreeUsedToCLustering {
    @Test
    public void mause0Test() throws WrongDimensionException {
        CEC cec = new CEC();
        String src = "src/main/resources/datat/mouse_1/input.txt";
        cec.setData(src, "text/space-separated-values");
        Data data = cec.getData();
        AbstractKDTree tree = new SimpleBinaryKDTree(data.getData());
        List<List<Point>> pointsToDisplay = new ArrayList<>();
        pointsToDisplay.add(data.getData());
        new Imagek(pointsToDisplay).disp();
        for (int i = 1; i < 6; ++i) {
            new Imagek(tree.getClusters(i)).disp(String.valueOf(i));
        }
        Scanner sc = new Scanner(System.in);
        sc.next();
    }

    @Test
    public void mause1Test() throws WrongDimensionException {
        CEC cec = new CEC();
        String src = "src/main/resources/datat/mouse_fix_r_1/input.txt";
        cec.setData(src, "text/space-separated-values");
        Data data = cec.getData();
        AbstractKDTree tree = new SimpleBinaryKDTree(data.getData());
        List<List<Point>> pointsToDisplay = new ArrayList<>();
        pointsToDisplay.add(data.getData());
        new Imagek(pointsToDisplay).disp();
        for (int i = 1; i < 6; ++i) {
            new Imagek(tree.getClusters(i)).disp(String.valueOf(i));
        }
        Scanner sc = new Scanner(System.in);
        sc.next();
    }

    @Test
    public void mause2Test() throws WrongDimensionException {
        CEC cec = new CEC();
        String src = "src/main/resources/datat/mouse_fix_r_2/input.txt";
        cec.setData(src, "text/space-separated-values");
        Data data = cec.getData();
        AbstractKDTree tree = new SimpleBinaryKDTree(data.getData());
        List<List<Point>> pointsToDisplay = new ArrayList<>();
        pointsToDisplay.add(data.getData());
        new Imagek(pointsToDisplay).disp();
        for (int i = 1; i < 6; ++i) {
            new Imagek(tree.getClusters(i)).disp(String.valueOf(i));
        }
        Scanner sc = new Scanner(System.in);
        sc.next();
    }

    @Test
    public void mause3Test() throws WrongDimensionException {
        CEC cec = new CEC();
        String src = "src/main/resources/datat/mouse_fix_r_3/input.txt";
        cec.setData(src, "text/space-separated-values");
        Data data = cec.getData();
        AbstractKDTree tree = new SimpleBinaryKDTree(data.getData());
        List<List<Point>> pointsToDisplay = new ArrayList<>();
        pointsToDisplay.add(data.getData());
        new Imagek(pointsToDisplay).disp();
        for (int i = 1; i < 6; ++i) {
            new Imagek(tree.getClusters(i)).disp(String.valueOf(i));
        }
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
