import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;


public class DemoJzy3dFX extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(DemoJzy3dFX.class.getSimpleName());

        // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart  = getDemoChart(factory, "offscreen");
        ImageView imageView = factory.bindImageView(chart);

        // JavaFX
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        pane.getChildren().add(imageView);

        factory.addSceneSizeChangedListener(chart, scene);

        stage.setWidth(500);
        stage.setHeight(500);
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {
        HashMap<Integer, ArrayList<Coord3d>> pointsetMap = new HashMap<>();
        String pointsStrPath = "src/main/resources/datat/ellipsoid/data.txt";
        Path pointsPath = Paths.get(pointsStrPath);
        String labelsStrPath = "src/main/resources/datat/ellipsoid/label.txt";
        Path labelsPath = Paths.get(labelsStrPath);

        try (Stream<String> stream = Files.lines(pointsPath)) {
            Iterator<String> iter = stream.iterator();
            try (Stream<String> labelStream = Files.lines(labelsPath)) {
                Iterator<String> labelIter = labelStream.iterator();
                while (iter.hasNext() && labelIter.hasNext()) {
                    String[] coords = iter.next().split(" ");
                    Coord3d coord = new Coord3d(Float.valueOf(coords[0]), Float.valueOf(coords[1]), Float.valueOf(coords[2]));
                    Integer labelId = Integer.valueOf(labelIter.next());
                    if (!pointsetMap.containsKey(labelId)) {
                        pointsetMap.put(labelId, new ArrayList<>());
                    }
                    pointsetMap.get(labelId).add(coord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Scatter> scatters = new ArrayList<>();
        for (ArrayList<Coord3d> points : pointsetMap.values()) {
            Scatter scatter = new Scatter(points.toArray(new Coord3d[0]), Color.random());
            scatters.add(scatter);
        }

        Quality quality = Quality.Advanced;
        //quality.setSmoothPolygon(true);
        //quality.setAnimated(true);
        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        Graph graph = chart.getScene().getGraph();
        for (Scatter scatter : scatters) {
            graph.add(scatter);
        }
        return chart;
    }
}