package cec.input;

import cec.cluster.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Krzysztof
 */
public class TextReader extends DataReader {

    private final String type1 = "text/tab-separated-values";
    private final String type2 = "text/space-separated-values";

    public TextReader() {

    }

    @Override
    public boolean type(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        String separator = "";
        switch (type) {
            case type1:
                separator = "\t";
                break;
            case type2:
                separator = " ";
                break;
            default:
                if (successor != null) {
                    return successor.read(filename, type);
                } else {
                    throw new RuntimeException("File type unsupported");
                }
        }

        List<Point> data = new ArrayList<>();

        final double weight = 1. / countLines(filename);

        FileReader fileReader = new FileReader(filename);

        final int dim;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            data = new CopyOnWriteArrayList<>();
            String line = null;
            if ((line = bufferedReader.readLine()) != null) {
                dim = getDimension(line, separator);
            } else {
                throw new RuntimeException("Empty file " + filename);
            }

            do {
                final String[] ls = line.split(separator);
                if (dim != ls.length) {
                    throw new RuntimeException("Dimension error in data file");
                } else {
                    data.add(new Point(weight, ls));
                }
            } while ((line = bufferedReader.readLine()) != null);
        }

        return data;

    }

    private int countLines(String filename) throws IOException {
        int cnt = 0;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(filename))) {
            while (reader.readLine() != null) {
            }
            cnt = reader.getLineNumber();
        }
        return cnt;
    }

    private static int getDimension(String line, String separator) {
        return line.split(separator).length;
    }

}
