package utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by shure on 2017/4/12.
 */
public class ReaderImages {
    public static void main (String [] args){
        try {
            File file = new File("E:\\images\\qqqq.png");

            BufferedImage image = readerImages(file);
            ImageIO.write(image, "JPEG", new File("e:\\images\\example-rgb3.jpg"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取文件，若是 CMYK 模式的图片，转换为 RGB 模式
     * @param file
     * @return
     * @throws Exception
     */
    public static BufferedImage readerImages(File file) throws Exception{
        Iterator readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        while (readers.hasNext()) {
            reader = (ImageReader) readers.next();
            if (reader.canReadRaster()) {
                break;
            }
        }

        try {
            // 设置input.
            ImageInputStream input = ImageIO.createImageInputStream(file);
            reader.setInput(input);
        }catch (IOException ie){
            return null;
        }

        // 创建图片.
        BufferedImage image;
        try {
            // 尝试读取图片 (包括颜色的转换).
            image = reader.read(0);
        }catch (IOException e){
            // 读取Raster (没有颜色的转换).
            Raster raster = reader.readRaster(0, null);

            // 随意选择一个BufferedImage类型.
            int imageType;
            switch (raster.getNumBands()) {
                case 1:
                    imageType = BufferedImage.TYPE_BYTE_GRAY;
                    break;
                case 3:
                    imageType = BufferedImage.TYPE_3BYTE_BGR;
                    break;
                case 4:
                    imageType = BufferedImage.TYPE_4BYTE_ABGR;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            // 创建一个BufferedImage.
            image = new BufferedImage(raster.getWidth(), raster.getHeight(), imageType);

            // 设置图片数据.
            image.getRaster().setRect(raster);
        }

        return image;
    }
}
