package com.ebay.quality.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.apache.commons.io.IOUtils;

import com.ebay.quality.utils.PageDiffUtil;

public class PhantomJSMgmt {

	static Logger LOGGER = Logger.getLogger(PhantomJSMgmt.class);
	public String getPhantomJSBinaryFilePath() {
		String phantomJSBinPath = getOsBasedPahntomJSBinPath();
		InputStream is = PhantomJSMgmt.class
				.getResourceAsStream(phantomJSBinPath);
		OutputStream out;
		String phantomJSBinaryFilePath = "phantomjs"+PageDiffUtil.getFileExt();// Copy the phantomjs
														// binary file to the
														// root of the project
														// directory
		File phantomJS = new File(phantomJSBinaryFilePath);
		try {
			if (!phantomJS.exists()) {
				out = new FileOutputStream(phantomJSBinaryFilePath);
				IOUtils.copy(is, out);
				out.close();
				is.close();
				String errorsWarn=null;
				Process process = Runtime.getRuntime().exec("chmod +x " + phantomJSBinaryFilePath);
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
		return phantomJSBinaryFilePath;
	}

	public String getCaptureJSBinaryFilePath() {
		InputStream is = PhantomJSMgmt.class
				.getResourceAsStream(PageDiffUtil.CAPTURE_SS_JS);
		OutputStream out;
		String captureSsJSFileName = "captureSS.js";
		try {

			out = new FileOutputStream(captureSsJSFileName);
			IOUtils.copy(is, out);
			out.close();
			is.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return captureSsJSFileName;
	}
	
	public String getOsBasedPahntomJSBinPath(){
		String osName = PageDiffUtil.getOSName();
		String binPath = null;
		if(osName!=null && osName.toLowerCase().equals("mac")){
			binPath = PageDiffUtil.PHANTOMJS_BIN_PATH_MAC;
		}else if(osName!=null && osName.toLowerCase().equals("linux")){
			binPath = PageDiffUtil.PHANTOMJS_BIN_PATH_LINUX;
		}else if(osName!=null && osName.toLowerCase().equals("win")){
			binPath = PageDiffUtil.PHANTOMJS_BIN_PATH_WIN;
		}
		return binPath;
	}
}
