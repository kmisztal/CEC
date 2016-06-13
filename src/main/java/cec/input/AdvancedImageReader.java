package cec.input;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import cec.cluster.Point;

public interface AdvancedImageReader {

   public List<Point> read(String filename) throws IOException;
   //TODO: Here you can add methods for other types of images
}