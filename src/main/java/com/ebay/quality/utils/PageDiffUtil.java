package com.ebay.quality.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.ebay.quality.compare.ScreenShotComperator;
import com.ebay.quality.config.PageDiffClineArgs;
import com.ebay.quality.io.ScreenShotWriter;
import com.ebay.quality.io.ScreenShotWriterImpl;

public class PageDiffUtil {

	static Logger LOGGER = Logger
			.getLogger(ScreenShotComperator.class);

	public static final String PATH_SEPARATOR = "/";
	public static final String PATH_SUFFIX = "screenshots";
	public static final String URL_TYPE_SOURCE = "Source";
	public static final String URL_TYPE_TARGET = "Target";
	public static final String PHANTOMJS_BIN_PATH_LINUX = "/phantomjsbinary/phantomjs_1_9_7_linux/bin/phantomjs";
	public static final String PHANTOMJS_BIN_PATH_MAC = "/phantomjsbinary/phantomjs_1_9_7_mac/bin/phantomjs";
	public static final String PHANTOMJS_BIN_PATH_WIN = "/phantomjsbinary/phantomjs_1_9_7_win/phantomjs.exe";
	public static final String IMAGE_MAGIC_BIN_PATH_MAC = "/imagemagicbinary/ImageMagick-6_8_9_7_Mac/imagemagick/6.8.9-5/bin";
	public static final String IMAGE_MAGIC_BIN_PATH_WIN = "/imagemagicbinary/ImageMagick-6_8_9_7_Win";
	public static final String IMAGE_FILE_EXT_JPG = ".jpg";
	public static final String IMAGE_FILE_EXT_PNG = ".png";
	public static final String MAC_UNX_SOL_SCRIPT_PRFX = "./";
	public static final String IMAGE_FUZZINESS = "-fuzz 5%";
	public static final String DIFF_IMAGE_SUFX = "d";
	public static final String TARGT_IMAGE_SUFX = "t";
	public static final String COMBND_IMAGE_SUFX = "c";
	public static final String IMAGE_ADJOIN = "-adjoin";
	public static final String IMAGE_GEOMETRY = "-geometry";
	public static final String IMAGE_FRAME = "-frame 5";
	public static final String FINAL_DIFF_DIR = "result";
	public static final String CAPTURE_SS_JS = "/captureSS.js";
	public static final String FILE_NAME_PRFX = "IMG";
	public static final String THUMB_NAIL_SIZE = "100x150";
	public static final String WIN_EXEC_FILE_EXT = ".exe";
	public static final String HIGHLIGHT_DIFF_COLOR="-highlight-color blue";
	

	public static String getAbsoluteImageFilePath(
			ScreenShotWriterImpl scrWriter, String typeOfImage,
			String browser) {
		String fileName = null;
		String timeStamp = getTimeStamp();
		String dirPath = null;
		if (browser != null && browser.length() > 0) {
			if (browser.toLowerCase().contains("mobile"))
				dirPath = scrWriter.getMobileDir();
			else if (browser.toLowerCase().contains("tablet"))
				dirPath = scrWriter.getTabletDir();
			else
				dirPath = scrWriter.getDesktopDir();
			fileName = dirPath + PATH_SEPARATOR + FILE_NAME_PRFX + "_"
					+ timeStamp + typeOfImage + IMAGE_FILE_EXT_PNG;
			LOGGER.info("Absolute Image file path:" + fileName);
		} else
			LOGGER.info(
					"Can't get absolute file path. Device resolution is not correct:"
							+ browser);
		return fileName;
	}

	public static boolean isUnixMacOrSolarisOS() {
		String OS = System.getProperty("os.name").toLowerCase();
		LOGGER.info("Current OS:" + OS);
		return ((OS.indexOf("mac") >= 0) || (OS.indexOf("nix") >= 0)
				|| (OS.indexOf("nux") >= 0) || (OS.indexOf("aix") > 0) || (OS
				.indexOf("sunos") >= 0));
	}
	
	public static String getOSName() {
		String osName=null;
		String OS = System.getProperty("os.name").toLowerCase();
		LOGGER.info( "Current OS:" + OS);
		if(OS.indexOf("mac") >= 0)
			osName="mac";
		else if(OS.indexOf("nix") >= 0)
			osName="unix";	
		else if(OS.indexOf("nux") >= 0)
			osName="linux";
		else if(OS.indexOf("aix") >= 0)
			osName="aix";
		else if(OS.indexOf("sunos") >= 0)
			osName="solaris";
		else if(OS.indexOf("win") >= 0)
			osName="win";
		return osName;
	}
	
	public static String getFileExt() {
		String OS = System.getProperty("os.name").toLowerCase();
		LOGGER.info("Current OS:" + OS);
		if(OS.indexOf("win") >= 0)
			return PageDiffUtil.WIN_EXEC_FILE_EXT;
		else
			return "";
	}

	public static String getAbsoluteDiffFileName(String baseFileName) {
		String dirPath = null;
		String diffFileName = null;
		String timeStamp = getTimeStamp();
		if (baseFileName != null && baseFileName.length() > 0) {
			dirPath = baseFileName.substring(0, baseFileName.indexOf(".") - 1);
			diffFileName = dirPath + DIFF_IMAGE_SUFX + IMAGE_FILE_EXT_PNG;
			LOGGER.info( "Absolute diff file path:" + diffFileName
					);
		}
		return diffFileName;

	}
	
	public static String getAbsoluteTargetFileName(String baseFileName) {
		String dirPath = null;
		String targtFileName = null;
		String timeStamp = getTimeStamp();
		if (baseFileName != null && baseFileName.length() > 0) {
			dirPath = baseFileName.substring(0, baseFileName.indexOf(".") - 1);
			targtFileName = dirPath + TARGT_IMAGE_SUFX + IMAGE_FILE_EXT_PNG;
			LOGGER.info( "Absolute diff file path:" + targtFileName
					);
		}
		return targtFileName;

	}

	public static String getCombinedFileName(String baseFileName) {
		String tmpFileName = null;
		String dirPath = null;
		String combinedFileName = null;
		String timeStamp = getTimeStamp();
		if (baseFileName != null && baseFileName.length() > 0) {
			dirPath = baseFileName.substring(0, baseFileName.indexOf(".") - 1);
			tmpFileName = dirPath.substring(dirPath.indexOf("IMG"));
			dirPath = dirPath.substring(0, dirPath.indexOf("IMG") - 1);
			combinedFileName = dirPath + PATH_SEPARATOR + FINAL_DIFF_DIR
					+ PATH_SEPARATOR + tmpFileName + COMBND_IMAGE_SUFX
					+ IMAGE_FILE_EXT_PNG;
			LOGGER.info("Absolute diff file path:"
					+ combinedFileName);
		}
		return combinedFileName;

	}

	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
		String timeStamp = DATE_FORMAT.format(date);
		return timeStamp;
	}

	public static String getViewportSize(String browser,
			PageDiffClineArgs runTimeArgs) {
		String viewPortSize = null;
		if (browser != null && browser.length() > 0) {
			if (browser.toLowerCase().contains("mobile"))
				viewPortSize = runTimeArgs.mobileResolution;
			else if (browser.toLowerCase().contains("tablet"))
				viewPortSize = runTimeArgs.tabletResolution;
			else
				viewPortSize = runTimeArgs.desktopResolution;
		}
		return viewPortSize;
	}
	
	public static boolean isFileNamesMatches(String sourceFileName,
			String targetFileName) {
		String actualSourceFileName = null;
		String actualTargetFileName = null;
		boolean matched = false;
		if(sourceFileName!=null && targetFileName!=null && sourceFileName.length()>0 && targetFileName.length()>0){
			actualSourceFileName = sourceFileName.substring(sourceFileName.indexOf("IMG"),sourceFileName.indexOf(".")-1);
			actualTargetFileName = targetFileName.substring(targetFileName.indexOf("IMG"),targetFileName.indexOf(".")-1);
			if(actualSourceFileName.equals(actualTargetFileName))
				matched = true;	
			else
				LOGGER.warn( "File name did not match. Source File:"+sourceFileName+" Target file:"+targetFileName);
		}
		return matched;
		
	}
	
	public static boolean isFileNamesMatches(String sourceFileName,
			String targetFileName, String diffNameFile) {
		String actualSourceFileName = null;
		String actualTargetFileName = null;
		String actualDiffFileName = null;
		boolean matched = false;
		if(sourceFileName!=null && targetFileName!=null && diffNameFile!=null && sourceFileName.length()>0 && targetFileName.length()>0 && diffNameFile.length()>0){
			actualSourceFileName = sourceFileName.substring(sourceFileName.indexOf("IMG"),sourceFileName.indexOf(".")-1);
			actualTargetFileName = targetFileName.substring(targetFileName.indexOf("IMG"),targetFileName.indexOf(".")-1);
			actualDiffFileName = diffNameFile.substring(diffNameFile.indexOf("IMG"),diffNameFile.indexOf(".")-1);
			if(actualSourceFileName.equals(actualTargetFileName) && actualSourceFileName.equals(actualDiffFileName) && actualTargetFileName.equals(actualDiffFileName))
				matched = true;	
			else
				LOGGER.warn("File name did not match. Source File:"+sourceFileName+" Target file:"+targetFileName);
		}
		return matched;
		
	}
	
	public static void printProjectName(){
		System.out.println("***********************************************");
		System.out.println("*               \\\\  // ..      ..             *");
		System.out.println("*                \\\\//  || \\\\// || |\\          *");
		System.out.println("*                 \\/   ||  \\/  || |/          *");
		System.out.println("*           ☺☺☺☺☺☺☺STARTED☺☺☺☺☺☺☺☺☺  *");
		System.out.println("***********************************************");
	}
}
