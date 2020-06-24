package reader;

import cec.cluster.Point;
import cec.input.TextReader;
import junit.framework.TestCase;

import java.util.List;

public class TextReaderTest extends TestCase {
    public void testRead() throws Exception {
        TextReader tr = new TextReader();
        List<Point> list = tr.read("src/main/resources/data_test/csv/1.csv", "text/comma-separated-values");
        assert list.size() == 6;
        assert list.get(0).getDimension() == 5;

        list = tr.read("src/main/resources/data_test/csv/2.csv", "text/comma-separated-values");
        assert list.size() == 6;
        assert list.get(0).getDimension() == 5;

        list = tr.read("src/main/resources/data_test/csv/3.csv", "text/comma-separated-values");
        assert list.size() == 6;
        assert list.get(0).getDimension() == 5;
    }

}