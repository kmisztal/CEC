
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Cylinder;
import org.jzy3d.plot3d.primitives.Ellipsoid;
import org.jzy3d.plot3d.rendering.canvas.Quality;


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
        final Cylinder cylinder = new Cylinder();
        Coord3d position = new Coord3d(0, 0, 0);
        Color color = new Color(0, 0, 255);
        cylinder.setData(position, 100, 10, 10, 10, color);

        Coord3d rotationVec = new Coord3d(1, 1, 1);
        float angle = 45;
        final Ellipsoid ellipsoid = new Ellipsoid(position, 100, 200, 300, 30, new Coord3d(1, 1, 0));
        final Ellipsoid ellipsoid1 = new Ellipsoid(position, 300, 200, 100, angle, rotationVec);
//        ellipsoid.setData(100, 100, 100);

        Quality quality = Quality.Advanced;
        //quality.setSmoothPolygon(true);
        //quality.setAnimated(true);

        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
//        chart.getScene().getGraph().add(surface);
//        chart.getScene().getGraph().add(cylinder);
        chart.getScene()
                .getGraph()
                .add(ellipsoid);
        chart.getScene().getGraph().add(ellipsoid1);
        return chart;
    }
}