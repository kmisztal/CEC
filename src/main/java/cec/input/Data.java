package cec.input;

import cec.cluster.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krzysztof
 */
public class Data {

    private List<Point> data;
    private final DataReader reader;

    public Data() {
        reader = new TextReader();
        reader.setSuccessor(new ImageReader());
    }

    public void read(String filename, String type){
        try {
            data = reader.read(filename, type);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
    public Iterator<Point> iterator() {
        return data.iterator();
    }

    public int getSize() {
        return data.size();
    }

    public int getDimension() {
        return data.get(0).getDimension();
    }

    public List<Point> getData() {
        return data;
    }

    public Point get(int i) {
        return data.get(i);
    }

    @Override
    public String toString() {
        if(data.isEmpty())
            return "Empty dataset";
        return "Data: size => " + getSize() + "; dimension => " + getDimension();
    }

    public enum DataType {
        TEXT_TAB("text/tab-separated-values", "Text file with real values separated by a single tab character."),
        TEXT_SPACE("text/space-separated-values", "Text file with real values separated by a single space character."),
        TEXT_CSV("text/comma-separated-values", "Text file with real values separated by a single comma character. Note that some locales use the comma character as a decimal point while the CSV format separator is a semicolon character. Thic CSV format is currently not supported."),
        IMAGE_PNG("image/png", "A PNG file. Maps pixels to points in a two-dimensional space with point weights proportional to the pixels' shades in grayscale.");


        private final String description;
        private final String identifier;

        public String getDescription() {
            return description;
        }

        public String getIdentifier() {
            return identifier;
        }

        public static DataType getByIdentifier(String id) {
            for (DataType dt : values())
                if (dt.getIdentifier().equals(id))
                    return dt;
            return null;
        }

        DataType(String identifier, String description) {
            this.identifier = identifier;
            this.description = description;
        }
    }


}