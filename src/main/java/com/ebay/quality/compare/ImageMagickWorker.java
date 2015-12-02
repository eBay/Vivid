package com.ebay.quality.compare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * This class implements the Callable interface to implement
 * worker agents for Imagemagick related tasks
 * @author kvikram
 *
 */
public class ImageMagickWorker implements Callable {
	 static Logger LOGGER = Logger.getLogger(ImageMagickWorker.class);
private  String imageMagickCmd=null;
	
	public ImageMagickWorker(String imageMgckCmd){
		this.imageMagickCmd = imageMgckCmd;
	}

	public String call() {
		String errorsWarn=null;
		Process process=null;
		try {
			process = Runtime.getRuntime().exec(imageMagickCmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader stdWarn = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		try {
			while ((errorsWarn = stdWarn.readLine()) != null) {
				LOGGER.warn( errorsWarn);
			}
			while ((errorsWarn = stdError.readLine()) != null) {
				LOGGER.warn(errorsWarn);
			}
			process.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "sucess";
	}

}
