package util;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class ReduceImg {

	public static void main(String[] args) {
		String filePath = "E:\\images";
	    getFiles(filePath);
	}

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	static void getFiles(String filePath) {
		try {
			File root = new File(filePath);
			File[] files = root.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					/*
					 * 递归调用
					 */
					getFiles(file.getAbsolutePath());
				} else {
					// 如果是文件进行处理
					String fileName = file.getName();
					if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")|| fileName.endsWith(".png") || fileName.endsWith(".gif")){
						reduceImage(file);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reduceImage(File file) throws Exception{
		Image img = ImageIO.read(file);
//		int newWidth = (int) img.getWidth(null);
//		int newHeight = (int) img.getHeight(null);
		
		int outputWidth = 800;
		int outputHeight = 600;
		// 为等比缩放计算输出的图片宽度及高度
		double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1;
		double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1;
		// 根据缩放比率大的进行缩放控制
		double rate = rate1 > rate2 ? rate1 : rate2;
		int newWidth = (int) (((double) img.getWidth(null)) / rate);
		int newHeight = (int) (((double) img.getHeight(null)) / rate);
		
		String outPath = file.getParent()+ "\\" + file.getName().substring(0, file.getName().indexOf(".")) + "_min"+file.getName().substring(file.getName().indexOf("."), file.getName().length());
		BufferedImage tag = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight,Image.SCALE_SMOOTH), 0, 0, null);
		FileOutputStream out = new FileOutputStream(outPath);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(tag);
		out.close();
	}

}
