package cec.input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import cec.cluster.Point;

public class AdvancedImageReader {
	
	   public List<Point> read(String filename) throws IOException {
	       CopyOnWriteArrayList<Point> result = new CopyOnWriteArrayList<>();
	        BufferedImage bufferedImage = ImageIO.read(new File(filename));
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
