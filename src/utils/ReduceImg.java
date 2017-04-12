package utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;


public class ReduceImg {

	public static void main(String[] args) {
		String filePath = "E://images";
	    getFiles(filePath);
	}

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	private static void getFiles(String filePath) {
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
					String fileName = file.getName().toLowerCase();
					if(file.length() > 0 && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")|| fileName.endsWith(".png") || fileName.endsWith(".gif"))){
						// 读取文件
						BufferedImage image = ReaderImages.readerImages(file);
						// 文件后缀名
						String formatName = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
						// 文件输出路径
						String outPath = file.getParent() + "\\" + fileName.substring(0, fileName.lastIndexOf('.')) + "_min." + formatName;
						// 压缩
						if(image != null){
							reduceImage(image, outPath);
						}else {
							System.out.println("文件格式不正确！");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对图片进行等比例压缩
	 * @param img 图片文件
	 * @param outPath 图片输出路径
	 * @throws Exception
	 */
	public static void reduceImage(BufferedImage img, String outPath) throws Exception{

		int outputWidth = 800;
		int outputHeight = 600;
		// 为等比缩放计算输出的图片宽度及高度
		double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1;
		double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1;
		// 根据缩放比率大的进行缩放控制
		double rate = rate1 > rate2 ? rate1 : rate2;
		int newWidth = (int) (((double) img.getWidth(null)) / rate);
		int newHeight = (int) (((double) img.getHeight(null)) / rate);


		BufferedImage tag = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
		FileOutputStream out = new FileOutputStream(outPath);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(tag);
		out.close();
	}

}
