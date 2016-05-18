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


}