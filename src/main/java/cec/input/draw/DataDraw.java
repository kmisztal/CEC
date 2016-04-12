package cec.input.draw;

import cec.run.CECAtomic;
import java.awt.Color;
import javax.swing.JFrame;
import tools.ColorGenerator;

/**
 *
 * @author Krzysztof
 */
public class DataDraw extends JFrame {

    private final CECAtomic data;
    private Color[] colors = {Color.red, Color.green, Color.gray,
                                Color.magenta, Color.blue, Color.pink, 
                                Color.cyan, Color.orange, Color.yellow};

    public DataDraw(CECAtomic data) {
        this.data = data;

        if (this.data.getNumberOfClusters() > colors.length) {
            colors = ColorGenerator.randomColorArray(this.data.getNumberOfClusters());
        }
        LookAndFeel.doIt();
    }

    public void disp() {/*
        if (data.getDimension() != 2) {
            throw new RuntimeException("You can see only 2D data");
        }
        final int k = data.getNumberOfClusters();

        DataTable[] dt = new DataTable[k + 1 + data.getNumberOfClusters()];//new DataTable(Double.class)
        for (int i = 0; i < dt.length; ++i) {
            dt[i] = new DataTable(Double.class, Double.class);
        }

        data.getCLusters().stream().forEach((c) -> {
            c.getData().stream().forEach((p) -> {
                dt[c.getId()].add(p.getMean().get(0, 0), p.getMean().get(1, 0));
            });
        });

        XYPlot plot = new XYPlot(dt);

        int i = 0;
        for (; i < k; ++i) {
            DataSource s = plot.get(i);

            // Style data series
            PointRenderer points1 = new DefaultPointRenderer2D();
            points1.setShape(new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
            points1.setColor(colors[i]);
            plot.setPointRenderer(s, points1);
        }

        LineRenderer line2 = new SmoothLineRenderer2D();
        line2.setColor(GraphicsUtils.deriveWithAlpha(Color.black, 180));
        line2.setStroke(new BasicStroke(6));
        plot.setLineRenderer(dt[k + 1], line2);

        data.getCLusters().stream().filter((c) -> (!c.isEmpty())).map((c) -> c.getMean()).forEach((m) -> {
            dt[k].add(m.get(0, 0), m.get(1, 0));
        });

        for (Cluster c : data.getCLusters()) {
            ++i;
            if (c.isEmpty()) {
                continue;
            }
            Ellipse e = new Ellipse(c);
            e.addData(dt[i]);
            plot.setLineRenderer(dt[i], line2);
        }

        DataSource s = plot.get(k);

        // Style data series
        PointRenderer points1 = new DefaultPointRenderer2D();
        points1.setShape(new Ellipse2D.Double(-10.0, -10.0, 20.0, 20.0));
        points1.setColor(Color.BLACK);
        plot.setPointRenderer(s, points1);

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
        this.setVisible(true);*/
    }

}
