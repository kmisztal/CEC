package cec.input.draw;

import cec.CECRunner;
import cec.cluster.Cluster;
import cec.cluster.Point;
import cec.run.CECAtomic;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Krzysztof
 */
public class DataDraw extends JFrame {

    private final CECAtomic data;
    private final Color[] colors = {Color.red, Color.green, Color.gray, Color.magenta, Color.blue, Color.pink};//ColorGenerator.randomColorArray(10);

    public DataDraw(CECAtomic data) {
        this.data = data;
    }

    public void disp() {
        if (data.getDimension() != 2) {
            throw new RuntimeException("You can see only 2D data");
        }
        final int k = data.getNumberOfClusters();

        DataTable[] dt = new DataTable[k + 1];//new DataTable(Double.class)
        for (int i = 0; i < k + 1; ++i) {
            dt[i] = new DataTable(Double.class, Double.class);
        }

        data.getCLusters().stream().forEach((c) -> {
            c.getData().stream().forEach((p) -> {
                p.getMean();
                dt[c.getId()].add(p.getMean().get(0, 0), p.getMean().get(1, 0));
            });
        });

        XYPlot plot = new XYPlot(dt);

        for (int i = 0; i < k; ++i) {
            DataSource s = plot.get(i);

            // Style data series
            PointRenderer points1 = new DefaultPointRenderer2D();
            points1.setShape(new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
            points1.setColor(colors[i]);
            plot.setPointRenderer(s, points1);
        }

        int i = 0;
        for (Cluster c : data.getCLusters()) {
            if (!c.isEmpty()) {
                final SimpleMatrix m = c.getMean();
                dt[k].add(m.get(0, 0), m.get(1, 0));
            }
            ++i;
        }

        DataSource s = plot.get(k);

        // Style data series
        PointRenderer points1 = new DefaultPointRenderer2D();
        points1.setShape(new Ellipse2D.Double(-20.0, -20.0, 20.0, 20.0));
        points1.setColor(Color.BLACK);
        plot.setPointRenderer(s, points1);

        // Create data
//        DataTable dataTable = new DataTable(Double.class, Double.class, Double.class);
//
//        final int POINT_COUNT = 1000;
//        java.util.Random rand = new java.util.Random();
//        for (int i = 0; i < POINT_COUNT; i++) {
//            double x = rand.nextGaussian();
//            double y1 = rand.nextGaussian() + x;
//            double y2 = rand.nextGaussian() - x;
//            dataTable.add(x, y1, y2);
//        }
//
//        // Create series
//        DataSeries series1 = new DataSeries("Series 1", dataTable, 0, 1);
//        DataSeries series2 = new DataSeries("Series 2", dataTable, 0, 2);
//        XYPlot plot = new XYPlot(dataTable);//;series1, series2);
        // Style the plot
        double insetsTop = 20.0,
                insetsLeft = 60.0,
                insetsBottom = 60.0,
                insetsRight = 40.0;
        plot.setInsets(new Insets2D.Double(
                insetsTop, insetsLeft, insetsBottom, insetsRight));
        plot.getTitle().setText("Nice scatter");

        // Style the plot area
        plot.getPlotArea().setBorderColor(new Color(0.0f, 0.3f, 1.0f));
        plot.getPlotArea().setBorderStroke(new BasicStroke(2f));

        // Style data series
//        PointRenderer points1 = new DefaultPointRenderer2D();
//        points1.setShape(new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
//        points1.setColor(new Color(0.0f, 0.3f, 1.0f, 0.3f));
//        plot.setPointRenderer(series1, points1);
//
//        PointRenderer points2 = new DefaultPointRenderer2D();
//        points2.setShape(new Rectangle2D.Double(-2.5, -2.5, 5, 5));
//        points2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
//        plot.setPointRenderer(series2, points2);
        // Style axes
        plot.getAxisRenderer(XYPlot.AXIS_X).setLabel("X");
        plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel("Y");
        plot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(1.0);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(2.0);
        plot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);

        // Display on screen
        this.getContentPane().add(new InteractivePanel(plot), BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(getContentPane().getMinimumSize());
        this.setSize(550, 550);
        this.setVisible(true);
    }

//    public static void main(String[] args) {
//        DataDraw df = new DataDraw();
//        df.setVisible(true);
//    
//    }
}
