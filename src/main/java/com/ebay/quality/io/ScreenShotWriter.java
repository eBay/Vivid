package com.ebay.quality.io;

import java.io.File;

import com.ebay.quality.config.PageDiffClineArgs;
import com.ebay.quality.config.PageDiffRuntimeArgs;

public interface ScreenShotWriter {

	public void saveScreenShots(File file, String fileName);
	public void createScreenShotDirs(PageDiffClineArgs configs);
	
}
