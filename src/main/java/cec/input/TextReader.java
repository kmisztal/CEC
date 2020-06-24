package cec.input;

import cec.cluster.Point;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Krzysztof
 */
public class TextReader extends DataReader {

    private static final String type1 = "text/tab-separated-values";
    private static final String type2 = "text/space-separated-values";

    public TextReader() {

    }

    @Override
    public boolean type(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        String separator;
        Data.DataType inputType = Data.DataType.getByIdentifier(type);
        switch (inputType) {
            case TEXT_TAB:
                separator = "\t";
                break;
            case TEXT_SPACE:
                separator = " ";
                break;
            case TEXT_CSV:
                separator = ",";
                break;
            default:
                if (successor != null) {
                    return successor.read(filename, type);
                } else {
                    throw new RuntimeException("File type unsupported");
                }
        }

        List<Point> data;

        final double weight;


        InputStreamReader fileReader = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);


        final int dim;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            data = new CopyOnWriteArrayList<>();
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                dim = getDimension(line, separator);
            } else {
                throw new RuntimeException("Empty file " + filename);
            }
            if (line.matches(".*[a-zA-Z].*")) { //header present
                line = bufferedReader.readLine();
                weight = 1. / (countLines(filename) - 1);
            } else
                weight = 1. / countLines(filename);

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
        int cnt;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
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
