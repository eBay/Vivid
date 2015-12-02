package com.ebay.quality.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.apache.commons.io.FileUtils;

import com.ebay.quality.config.PageDiffClineArgs;
import com.ebay.quality.utils.PageDiffUtil;

public class ScreenShotWriterImpl implements ScreenShotWriter {

	private final static Logger LOGGER = Logger
			.getLogger(ScreenShotWriterImpl.class);
	private static String defaultDir = null;
	private static String resultDir = null;
	private static String mobileDir = null;
	private static String tabletDir = null;
	private static String desktopDir = null;
	private static String mobileResultDir = null;
	private static String tabletResultDir = null;
	private static String desktopResultDir = null;

	public void saveScreenShots(File file, String fileName) {

		if (file != null) {
			try {
				FileUtils.copyFile(file, new File(defaultDir
						+ PageDiffUtil.PATH_SEPARATOR + fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createScreenShotDirs(PageDiffClineArgs configs) {
		List<String> dirs = new ArrayList<String>();
		if (configs.writeScreenShotsAt != null
				&& configs.writeScreenShotsAt.length() > 0) {
			defaultDir = configs.writeScreenShotsAt;
			mobileDir = configs.writeScreenShotsAt
					+ PageDiffUtil.PATH_SEPARATOR + "mobile";
			tabletDir = configs.writeScreenShotsAt
					+ PageDiffUtil.PATH_SEPARATOR + "tablet";
			desktopDir = configs.writeScreenShotsAt
					+ PageDiffUtil.PATH_SEPARATOR + "desktop";
			mobileResultDir = mobileDir + PageDiffUtil.PATH_SEPARATOR
					+ "result";
			tabletResultDir = tabletDir + PageDiffUtil.PATH_SEPARATOR
					+ "result";
			desktopResultDir = desktopDir + PageDiffUtil.PATH_SEPARATOR
					+ "result";
			dirs.add(defaultDir);
			dirs.add(mobileDir);
			dirs.add(tabletDir);
			dirs.add(desktopDir);
			dirs.add(mobileResultDir);
			dirs.add(tabletResultDir);
			dirs.add(desktopResultDir);
		}
		for (String dirName : dirs) {
			if (dirName != null && dirName.length() > 0) {
				File directory = new File(dirName);
				if (directory.exists()) {
					LOGGER.info("Directory alreday exists:"
							+ dirName + " Skipping the directory creation....");
				} else {
					if (directory.mkdirs())
						LOGGER.info(
								"Successfully created directory:" + dirName);
					else
						LOGGER.info("Directory creation failed");
				}
			} else {
				LOGGER.info(
						"Directory creation failed. Given path is not a valid path:"
								+ dirName);
			}
		}

	}
	
	public void cleanUpOldScreenShots(){
		
	}

	public String getScreenShotsDirPath() {
		return defaultDir;
	}

	public static String getDesktopDir() {
		return desktopDir;
	}

	public static String getTabletDir() {
		return tabletDir;
	}

	public static String getMobileDir() {
		return mobileDir;
	}

	public static String getMobileResultDir() {
		return mobileResultDir;
	}

	public static String getTabletResultDir() {
		return tabletResultDir;
	}

	public static String getDesktopResultDir() {
		return desktopResultDir;
	}

}
