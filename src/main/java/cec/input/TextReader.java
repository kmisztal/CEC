package cec.input;

import cec.cluster.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Krzysztof
 */
public class TextReader extends DataReader {

    private static final String type1 = "text/tab-separated-values";
    private static final String type2 = "text/space-separated-values";

    private static final Logger logger = LoggerFactory.getLogger(TextReader.class);

    public TextReader() {
    }

    @Override
    public boolean type(String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        String separator;
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

        List<Point> data;

        final double weight = 1. / countLines(filename);


        InputStreamReader fileReader = getInputStreamReader(filename);

        final int dim;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            data = new CopyOnWriteArrayList<>();
            String line;
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
        int cnt;
        try (LineNumberReader reader = new LineNumberReader(getInputStreamReader(filename))) {
            while (reader.readLine() != null) {
            }
            cnt = reader.getLineNumber();
        }
        return cnt;
    }

    private InputStreamReader getInputStreamReader(String filename) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            logger.warn("File not found in filesystem: ", e.getMessage());
            logger.warn("Looking through bundled resources.");
            reader = new InputStreamReader(TextReader.class.getResourceAsStream("/" + filename),
                    StandardCharsets.UTF_8);
            if (reader == null) {
                logger.error("Could not find specified file {} in filesystem nor in bundled resources.", filename);
                throw new RuntimeException("File not found, see logs.");
            }
        }
        return reader;
    }

    private static int getDimension(String line, String separator) {
        return line.split(separator).length;
    }

}
