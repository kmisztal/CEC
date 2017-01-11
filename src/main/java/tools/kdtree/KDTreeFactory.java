package tools.kdtree;

import cec.CEC;
import cec.cluster.Cluster;
import cec.cluster.Point;
import cec.cluster.types.ClusterKind;
import tools.kdtree.exceptions.WrongDimensionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KDTreeFactory {

    private static void checkPoints(List<Point> points) throws WrongDimensionException {
        if (points.size() == 0) {
            throw new IllegalArgumentException("Input list is empty");
        }
        int dimension = points.get(0).getDimension();
        if (!points.stream().allMatch(p-> p.getDimension() == dimension)) {
            throw new WrongDimensionException("At least one point has inproper dimension");
        }
    }

    public static KDTree createSimpleBinaryKDTree(List<Point> points) throws WrongDimensionException {
        checkPoints(points);
        return new AbstractKDTree(points) {
            @Override
            protected int getDivisionCoordinate(List<Point> points, int depth) {
                return depth % dimension;
            }

            @Override
            protected List<Integer> getDivisionIndecies(List<Point> points, int divisionCoordinate, int depth) {
                return Arrays.asList((points.size() % 2 == 0 ? points.size() / 2 : (points.size() + 1) / 2));
            }
        };
    }

    public static KDTree createCECClusteringKDTree(List<Point> points) throws WrongDimensionException, IOException {
        checkPoints(points);
        return new AbstractClusteringTree(points) {
            @Override
            protected int getDivisionCoordinate(List<Point> points, int depth) {
                return (1+depth) % dimension;
            }

            @Override
            protected List<Double> getDivisionValues(List<Point> points, int divisionCoordinate, int depth) throws IOException {
                CEC cec = new CEC();
                Path path = Paths.get("temp");
                List<String> lines = new ArrayList<>();
                for (Point p : points) {
                    String line = String.valueOf(p.get(divisionCoordinate));
//                    System.out.println(line);
                    lines.add(line);
                }
                Files.write(path, lines);
                cec.setData("temp", "text/space-separated-values");
                cec.add(ClusterKind.Gaussians, 30);
                cec.run();
                Files.deleteIfExists(path);
//                System.out.println("=============================================");
//                System.out.println("Number of clusters: " + cec.getResult().getNumberOfClusters());
//                System.out.println("Used number of clusters: " + cec.getResult().getUsedNumberOfClusters());
//                System.out.println(cec.getResult().getPartition());

                List<Cluster> clusters = cec.getResult().getClusters().stream()
                        .filter(c -> c.getMean().determinant() != 0.0 || c.getCov().determinant() != 0.0)
                        .sorted((c1,c2) -> Double.compare(c1.getMean().determinant(), c2.getMean().determinant()))
                        .collect(Collectors.toList());
//                clusters.sort((a,b) -> Double.compare(a.getMean().determinant(), b.getMean().determinant()));

                if (clusters.size() <= 1) {
                    return new ArrayList<>();
                }

                List<Double> results = new ArrayList<>();
                for (int i = 0; i < clusters.size() - 1; ++i) {

                    double m1 = clusters.get(i).getMean().determinant();
                    double m2 = clusters.get(i+1).getMean().determinant();

                    double s1 = clusters.get(i).getCov().determinant();
                    double s2 = clusters.get(i+1).getCov().determinant();

                    double p1 = clusters.get(i).getCost();
                    double p2 = clusters.get(i+1).getCost();
                    if (m1 == 0.0 && m2 == 0.0 && s1 == 0.0 && s2 == 0.0) {
                        continue;
                    }
                    System.out.println("--------------------------------------------");
                    System.out.printf("Klaster %d\n", i);
                    System.out.printf("Lewa średnia: %f\n", m1);
                    System.out.printf("Lewa kowariancja: %f\n", s1);
                    System.out.printf("Lewe p: %f\n", p1);
                    System.out.printf("Prawa średnia: %f\n", m2);
                    System.out.printf("Prawa kowariancja: %f\n", s2);
                    System.out.printf("Prawe p: %f\n", p2);



                    if (s1 == s2) {
                        double x = (m1*m1 - m2*m2 - 2*s1*s1*Math.log(p1/p2))/(2*m1 - 2*m2);
                        results.add(x);
                    } else {
//                        System.out.printf("Mianownik: %f\n", s1*s1 - s2*s2);
//                        System.out.printf("Logarytm: %f\n", Math.log(p1*s2/p2/s1));
                        double x1 = (m2*s1*s1 - s2*(s1*Math.sqrt((m1-m2)*(m1-m2) - 2*(s1*s1 - s2*s2)*Math.log(p1*s2/p2/s1)) + m1*s2))/(s1*s1 - s2*s2);
                        double x2 = (s1*s2*Math.sqrt((m1-m2)*(m1-m2) - 2*(s1*s1 - s2*s2)*Math.log(p1*s2/p2/s1)) - m1*s2*s2 + m2*s1*s1)/(s1*s1 - s2*s2);
                        if (m1 < x1 && x1 < m2) {
                            results.add(x1);
                        } else {
                            results.add(x2);
                        }
                    }
                }
                return results;
            }
        };
    }
}
