package reader;

import cec.cluster.Point;
import cec.input.DataReader;
import cec.input.ImageReader;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by pawel on 5/29/16.
 */
public class ImageReaderTest extends TestCase {
    public void testRead() throws Exception {
        DataReader imageReader = new ImageReader();
        List<Point> list = imageReader.read("src/main/resources/data_test/png/circles.png", "image/png");
        assert list.size() == 2036;
    }

    public void testReadTIFF() throws Exception {
        DataReader imageReader = new ImageReader();
        List<Point> list = imageReader.read("src/main/resources/data_test/tiff/example.tiff", "image/tiff");
        assertEquals(list.size(), 93902);
    }
    
    public void testReadBMP() throws Exception {
        DataReader imageReader = new ImageReader();
        List<Point> list = imageReader.read("src/main/resources/data_test/bmp/example.bmp", "image/bmp");
        assertEquals(list.size(), 116136);
    }
}