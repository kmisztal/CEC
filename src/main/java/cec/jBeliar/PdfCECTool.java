package cec.jBeliar;


import cec.cluster.Cluster;
import com.itextpdf.text.BaseColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Use in CECAtomic class
 */

public class PdfCECTool {

    private PdfCECTool() {}

    public static void save(List<Double> costs, int usedNumberOfClusters, int numberOfClusters, ArrayList<Cluster> clusters) {

        PDFData data = new PDFData();

        data.addSentence("");
        data.setTitle("BEST RUN INFO");
        data.addSentence("Completed in " + costs.size() + " steps");
        data.addSentence("Cost in each step " + costs.toString());
        data.addSentence(usedNumberOfClusters + " needed for clustering (while " + numberOfClusters + " suggested)");

        int no = 0;

        for (Cluster c : clusters) {
            if (c.isEmpty()) {
                continue;
            }

            data.addLineSeparator();


            data.addSentence("Cluster " + no++ + " " + c.getType());
            data.addSentence(c.getCardinality() + " points");
            data.addSentence("mean");
            data.addSentence(c.getMean().toString());
            data.addSentence("cov");
            data.addSentence(c.getCostFunction().getCov().toString());
        }

        data.addDate();

        PDFCreator creator = new PDFCreator(data,
                new PDFConfig("Damian", "Damian", "Results"),
                new PDFStyles(BaseColor.GRAY));
        creator.setFileName("CEC Raport");
        creator.create();
    }
}
