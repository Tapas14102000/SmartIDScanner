package com.TapasCodes;
import java.awt.Graphics2D;
import net.sourceforge.tess4j.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

public class ScannedImage {

	public static String processImg(BufferedImage ipimage,float scaleFactor,float offset)throws IOException, TesseractException
	{
		BufferedImage opimage = new BufferedImage(1050,1024,ipimage.getType());
		Graphics2D graphic = opimage.createGraphics();  
		graphic.drawImage(ipimage, 0, 0,1050, 1024, null);
		graphic.dispose();
		RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);
		BufferedImage fopimage = rescale.filter(opimage, null);
		File f=new File("E:\\TNP-CELL\\SmartIDScanner\\target\\classes\\static\\img\\output.png");
		ImageIO.write(fopimage,"jpg",f);
		ITesseract it = new Tesseract();
		it.setDatapath("tessdata");
		it.setLanguage("eng");
		String str = it.doOCR(fopimage);
		f.delete();
		return str;
	}
	public static String extract(String path) throws Exception
	{
		File f = new File(path);
		BufferedImage ipimage = ImageIO.read(f);
		double d = ipimage.getRGB(ipimage.getTileWidth() / 2,ipimage.getTileHeight() / 2);
		if (d >= -1.4211511E7 && d < -7254228) 
			return processImg(ipimage, 3f, -10f);
		else if (d >= -7254228 && d < -2171170) 
			return processImg(ipimage, 1.455f, -47f);
		else if (d >= -2171170 && d < -1907998) 
			return processImg(ipimage, 1.35f, -10f);
		else if (d >= -1907998 && d < -257) 
			return processImg(ipimage, 1.19f, 0.5f);
		else if (d >= -257 && d < -1) 
			return processImg(ipimage, 1f, 0.5f);
		else  
			return processImg(ipimage, 1f, 0.35f);
	}
	
}
