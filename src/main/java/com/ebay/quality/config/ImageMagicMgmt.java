package com.ebay.quality.config;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.ebay.quality.utils.PageDiffUtil;

public class ImageMagicMgmt {
	static Logger LOGGER = Logger.getLogger(ImageMagicMgmt.class);

	public static String getImageMagicConvertCmd() {
		String cmd = getOsBasedImageMagickBinPath()
				+ PageDiffUtil.PATH_SEPARATOR + "convert"+PageDiffUtil.getFileExt();
		LOGGER.info(cmd);
		return cmd;
	}

	public static String getImageMagicCompareCmd() {
		String cmd = getOsBasedImageMagickBinPath()
				+ PageDiffUtil.PATH_SEPARATOR + "compare"+PageDiffUtil.getFileExt();
		LOGGER.info(cmd);
		return cmd;
	}

	public static long getImageHeight(File imageFile) {
		BufferedImage bimg = null;
		if (imageFile != null)
			try {
				bimg = ImageIO.read(imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return bimg.getHeight();
	}

	public static long getImageWidth(File imageFile) {
		BufferedImage bimg = null;
		if (imageFile != null)
			try {
				bimg = ImageIO.read(imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return bimg.getWidth();
	}

	public static String getConvertBinFilePath() {
		InputStream is = ImageMagicMgmt.class
				.getResourceAsStream(getOsBasedImageMagickBinPath()
						+ PageDiffUtil.PATH_SEPARATOR + "convert"+PageDiffUtil.getFileExt());
		OutputStream out;
		String convertFilePath = "convert"+PageDiffUtil.getFileExt();// Copy the convert binary file to
		// the root of the project directory
		File convertFile = new File(convertFilePath);
		try {
			if (!convertFile.exists()) {
				out = new FileOutputStream(convertFilePath);
				IOUtils.copy(is, out);
				out.close();
				is.close();
				String errorsWarn=null;
				Process process = Runtime.getRuntime().exec("chmod +x " + convertFilePath);
				BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((errorsWarn = stdWarn.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					while ((errorsWarn = stdError.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					process.destroy();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return convertFilePath;
	}

	public static String getCompareBinFilePath() {
		InputStream is = ImageMagicMgmt.class
				.getResourceAsStream(getOsBasedImageMagickBinPath()
						+ PageDiffUtil.PATH_SEPARATOR + "compare"+PageDiffUtil.getFileExt());
		OutputStream out;
		String compareFilePath = "compare"+PageDiffUtil.getFileExt();// Copy the compare binary file to
		// the root of the project directory
		File compareFile = new File(compareFilePath);
		try {
			if (!compareFile.exists()) {
				out = new FileOutputStream(compareFilePath);
				IOUtils.copy(is, out);
				out.close();
				is.close();
				String errorsWarn=null;
				Process process = Runtime.getRuntime().exec("chmod +x " + compareFilePath);
				BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((errorsWarn = stdWarn.readLine()) != null) {
						LOGGER.warn( errorsWarn);
					}
					while ((errorsWarn = stdError.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					process.destroy();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return compareFilePath;
	}

	public static String getMontageBinFilePath() {
		InputStream is = ImageMagicMgmt.class
				.getResourceAsStream(getOsBasedImageMagickBinPath()
						+ PageDiffUtil.PATH_SEPARATOR + "montage"+PageDiffUtil.getFileExt());
		OutputStream out;
		String montageFilePath = "montage"+PageDiffUtil.getFileExt();// Copy the montage binary file to
		// the root of the project directory
		File compareFile = new File(montageFilePath);
		try {
			if (!compareFile.exists()) {
				out = new FileOutputStream(montageFilePath);
				IOUtils.copy(is, out);
				out.close();
				is.close();
				String errorsWarn=null;
				Process process = Runtime.getRuntime().exec("chmod +x " + montageFilePath);
				BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((errorsWarn = stdWarn.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					while ((errorsWarn = stdError.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					process.destroy();
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return montageFilePath;
	}

	public static String getMogrifyBinFilePath() {
		InputStream is = ImageMagicMgmt.class
				.getResourceAsStream(getOsBasedImageMagickBinPath()
						+ PageDiffUtil.PATH_SEPARATOR + "mogrify"+PageDiffUtil.getFileExt());
		OutputStream out;
		String mogrifyFilePath = "mogrify"+PageDiffUtil.getFileExt();// Copy the mogrify binary file to
		// the root of the project directory
		File mogrifyFile = new File(mogrifyFilePath);
		try {
			if (!mogrifyFile.exists()) {
				out = new FileOutputStream(mogrifyFilePath);
				IOUtils.copy(is, out);
				out.close();
				is.close();
				String errorsWarn=null;
				Process process = Runtime.getRuntime().exec("chmod +x " + mogrifyFilePath);
				BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((errorsWarn = stdWarn.readLine()) != null) {
						LOGGER.warn(errorsWarn);
					}
					while ((errorsWarn = stdError.readLine()) != null) {
						LOGGER.warn( errorsWarn);
					}
					process.destroy();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return mogrifyFilePath;
	}
	
	public static String getOsBasedImageMagickBinPath(){
		String osName = PageDiffUtil.getOSName();
		String binPath = null;
		if((osName!=null && osName.toLowerCase().equals("mac")) || (osName!=null && osName.toLowerCase().equals("linux"))){
			binPath = PageDiffUtil.IMAGE_MAGIC_BIN_PATH_MAC;
		}else if(osName!=null && osName.toLowerCase().equals("win")){
			binPath = PageDiffUtil.IMAGE_MAGIC_BIN_PATH_WIN;
		}
		return binPath;
	}
}
