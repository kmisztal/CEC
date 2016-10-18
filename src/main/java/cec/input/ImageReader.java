package cec.input;

import cec.cluster.Point;
import cec.input.AdvancedImageAdapter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Krzysztof
 */
public class ImageReader extends DataReader{

    public ImageReader() {

    }

    @Override
    public boolean type(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Point> read(String filename, String type) throws IOException {
        Data.DataType inputType = Data.DataType.getByIdentifier(type);
        if (inputType == null)
            throw new UnsupportedOperationException("Not supported yet.");
        switch (inputType) {
            case IMAGE_PNG:
                return readPNG(filename, type);
            case IMAGE_TIFF:
            case IMAGE_BMP:
            	AdvancedImageAdapter advancedImageAdapter = new AdvancedImageAdapter(type);
            	return advancedImageAdapter.read(filename);
            default:
                if (successor != null) {
                    return successor.read(filename, type);
                } else {
                    throw new RuntimeException("File type unsupported");
                }
        }
    }

    private static List<Point> readPNG(String filename, String type) throws IOException {
        CopyOnWriteArrayList<Point> result = new CopyOnWriteArrayList<>();

        String dir = System.getProperty("user.dir");

        BufferedImage bufferedImage = ImageIO.read(new File(filename));
        Raster raster = bufferedImage.getData().createTranslatedChild(0, 0);
        ColorModel colorModel = bufferedImage.getColorModel();


        int[] bitDepth = colorModel.getComponentSize();
        int channelCount = colorModel.getNumColorComponents();

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = bufferedImage.getRGB(j, i);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb) & 0xFF;
                double weight = 0;
                double tempSum = 0;
                tempSum += red;
                tempSum += green;
                tempSum += blue;
                tempSum = tempSum / 3;
                weight = (255 - tempSum) * alpha / 255 / 255;
                if (weight > 0.000_000_001)
                    result.add(new Point(weight, j, height - i));
            }
        }
        return result;
    }
}
