package com.ebay.quality.compare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.ebay.quality.utils.PageDiffUtil;

/**
 * This class implements the Callable interface to implement
 * worker agents for PhantomJS related tasks
 * @author kvikram
 *
 */
public class PhantomJSWorker implements Callable<String> {
	static Logger LOGGER = Logger.getLogger(PhantomJSWorker.class);
	private  String takeScreenShotCmd=null;
	public String threadName=null;
	
	public PhantomJSWorker(String takeSSCmd){
		this.takeScreenShotCmd = takeSSCmd;
		this.threadName = "phantomJSWorker" + PageDiffUtil.getTimeStamp();
	}

	public String call() {
		LOGGER.info(Thread.currentThread().getName()+" Started");
		String errorsWarn=null;
		String[] splitCmdArr = takeScreenShotCmd.split(" ");
		Process process=null;
		try {
			process = Runtime.getRuntime().exec(takeScreenShotCmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		try {
			while ((errorsWarn = stdWarn.readLine()) != null) {
				LOGGER.warn(errorsWarn);
			}
			while ((errorsWarn = stdError.readLine()) != null) {
				LOGGER.warn(errorsWarn);
			}
			process.destroy();
			LOGGER.info("Screenshot taken successfully:"+splitCmdArr[3]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(Thread.currentThread().getName()+" Finished");
		return splitCmdArr[3];
	}

}
