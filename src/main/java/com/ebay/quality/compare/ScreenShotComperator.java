package com.ebay.quality.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import org.apache.log4j.Logger;


import com.ebay.quality.config.PhantomJSMgmt;
import com.ebay.quality.config.ImageMagicMgmt;
import com.ebay.quality.config.PageDiffClineArgs;
import com.ebay.quality.gallery.Gallery;
import com.ebay.quality.io.ScreenShotWriterImpl;
import com.ebay.quality.utils.PageDiffUtil;

/**
 * This class contains all the core methods
 * @author kvikram
 *
 */
public class ScreenShotComperator {

	static Logger LOGGER = Logger.getLogger(ScreenShotComperator.class);
	private static ScreenShotWriterImpl scWriter = new ScreenShotWriterImpl();
	private static Queue<String> sourceScreenShots = new PriorityQueue<String>();
	private static Queue<String> targetScreenShots = new PriorityQueue<String>();
	private static Queue<String> thumbNails = new PriorityQueue<String>();
	private static Queue<String> diffImgs = new PriorityQueue<String>();
	private static Queue<String> combinedImgs = new PriorityQueue<String>();
	private static Queue<String> takeScreenShotCmdQ = new PriorityQueue<String>();
	private static Queue<String> resizeScreenShotCmdQ = new PriorityQueue<String>();
	private static Queue<String> compareScreenShotCmdQ = new PriorityQueue<String>();
	private static Queue<String> combineScreenShotCmdQ = new PriorityQueue<String>();
	private static Queue<String> thumbnailScreenShotCmdQ = new PriorityQueue<String>();
	private static String convertBinFilePath = ImageMagicMgmt.getConvertBinFilePath();
	private static String compareBinFilePath = ImageMagicMgmt.getCompareBinFilePath();
	private static String montageBinFilePath = ImageMagicMgmt.getMontageBinFilePath();
	private static String mogrifyBinFilePath = ImageMagicMgmt.getMogrifyBinFilePath();
	
    /**
     * This method basically processes the screenshots taken by 
     * prepareScreenShots method of this class
     * 
     */
	public static void processScreenShots() {
		Iterator<String> sourceSSItr = null;
		Iterator<String> targetSSItr = null;
		Iterator<String> diffSSItr = null;
		String sourceImageFile = null;
		String targetImageFile = null;
		String diffImageFile = null;
		String resizeCmd=null;
		String compareCmd=null;
		if (sourceScreenShots != null && targetScreenShots != null
				&& sourceScreenShots.size() > 0 && targetScreenShots.size() > 0
				&& sourceScreenShots.size() == targetScreenShots.size()) {
			sourceSSItr = sourceScreenShots.iterator();
			targetSSItr = targetScreenShots.iterator();
			while (sourceSSItr.hasNext() && targetSSItr.hasNext()) {
				sourceImageFile = sourceSSItr.next();
				targetImageFile = targetSSItr.next();
				if(PageDiffUtil.isFileNamesMatches(sourceImageFile, targetImageFile)){
					resizeCmd=resizeScreenShot(sourceImageFile, targetImageFile);
					if(resizeCmd!=null && resizeCmd.length()>0){
						String[] cmds = resizeCmd.split("~");
						resizeScreenShotCmdQ.add(cmds[0]);
						resizeScreenShotCmdQ.add(cmds[1]);
					}
					compareCmd=generateDiffImage(sourceImageFile, targetImageFile);
					compareScreenShotCmdQ.add(compareCmd);
				}
			}
		} else {
			LOGGER.info("Something went wrong while preparing the resize & compare command. Terminating the programme........");
			System.exit(1);
		}
		if (sourceScreenShots != null && targetScreenShots != null && diffImgs!=null 
				&& sourceScreenShots.size() > 0 && targetScreenShots.size() > 0 && diffImgs.size()>0
				&& sourceScreenShots.size() == targetScreenShots.size() && sourceScreenShots.size() == diffImgs.size() && targetScreenShots.size() == diffImgs.size()) {
			sourceSSItr = sourceScreenShots.iterator();
			targetSSItr = targetScreenShots.iterator();
			diffSSItr = diffImgs.iterator();
			while (sourceSSItr.hasNext() && targetSSItr.hasNext() && diffSSItr.hasNext()) {
				sourceImageFile = sourceSSItr.next();
				targetImageFile = targetSSItr.next();
				diffImageFile = diffSSItr.next();
				generateFullSizeCombinedImageAndThumbnails(sourceImageFile,targetImageFile, diffImageFile);
			}
		} else {
			LOGGER.info("Something went wrong while preparing the combined images command. Terminating the programme........");
			System.exit(1);
		}
		executeTasks();
	}
	
	/**
	 * This method is basically responsible for submitting tasks to thread pool
	 * After each success full execution of task Executor parses the result
	 * and sends back the status of the task which is being use to decide whether to
	 * execute next steps or not
	 */
	public static void executeTasks(){
		boolean isTaskcompleted = false;
		Executor exec = new Executor();
		LOGGER.info("size of resizeScreenShotCmdQ:"+resizeScreenShotCmdQ.size());
			if(resizeScreenShotCmdQ!=null && resizeScreenShotCmdQ.size()>0){
				LOGGER.info("************************************* Resizing screen shots ************************************");
				isTaskcompleted = exec.parseResponse(exec.processImageMagickCommands(resizeScreenShotCmdQ));
			}
			else 
				isTaskcompleted=true;
			if(isTaskcompleted){
				isTaskcompleted=false;
				LOGGER.info("All images resized");
				LOGGER.info("************************************* Comparing screen shots ************************************");
				LOGGER.info("size of compareScreenShotCmdQ:"+compareScreenShotCmdQ.size());
				isTaskcompleted = exec.parseResponse(exec.processImageMagickCommands(compareScreenShotCmdQ));
				if(isTaskcompleted){
					isTaskcompleted=false;
					LOGGER.info("All diff images generated");
					LOGGER.info("************************************* Combining screen shots ************************************");
					isTaskcompleted = exec.parseResponse(exec.processImageMagickCommands(combineScreenShotCmdQ));
					if(isTaskcompleted){
						isTaskcompleted=false;
						LOGGER.info("All combined images generated");
						LOGGER.info("************************************* Generating thumbnails ************************************");
						isTaskcompleted = exec.parseResponse(exec.processImageMagickCommands(thumbnailScreenShotCmdQ));
						if(isTaskcompleted)
							LOGGER.info("All thumbnail images generated");
						else{
							LOGGER.error("Generate thumbnail task was not completed. Terminating the program.....");
							System.exit(1);
						}
					}else{
						LOGGER.error("Combine image task was not completed. Terminating the program.....");
						System.exit(1);
					}
					}else{
						LOGGER.error("Image comparison task was not completed. Terminating the program.....");
						System.exit(1);
					}
				}else{
					LOGGER.error("Image resize task was not completed. Terminating the program.....");
					System.exit(1);
				}
		}
	
	/**
	 * Responsible for taking screen shot using phantom js library
	 * and store them at predefined location
	 * @param runTimeArgs
	 * @return
	 */
	
	public static boolean prepareScreenShots(PageDiffClineArgs runTimeArgs) {

		
			PhantomJSMgmt ghostDriverMgmt = new PhantomJSMgmt();
			URL sourceUrl = null;
			URL targetUrl = null;
			String fileName = null;
			String viewPortSize = null;
			String phantomJsCmd = null;
			boolean isTaskcompleted = false;
			List<String> browsers = runTimeArgs.parsedUserAgentList;
			List<Map> urlMaps = runTimeArgs.parsedUrlPairList;
			String getCaptureJSBinPath=ghostDriverMgmt.getCaptureJSBinaryFilePath();
			String phantomJSBinPath=ghostDriverMgmt.getPhantomJSBinaryFilePath();
			try {
			if (browsers != null && urlMaps != null && browsers.size() > 0
					&& urlMaps.size() > 0) {
				for (String browser : browsers) {
					for (Map urlMap : urlMaps) {
						if (urlMap != null && urlMap.size() > 0) {
							sourceUrl = (URL) urlMap.get("Source");
							targetUrl = (URL) urlMap.get("Target");
							if (browser != null && browser.length() > 0) {
								viewPortSize = PageDiffUtil.getViewportSize(
										browser, runTimeArgs);
								LOGGER.info("Current SOURCE URL:"+ sourceUrl.toString());
								LOGGER.info("Current User Agent:"+ browser);
								fileName = PageDiffUtil
										.getAbsoluteImageFilePath(scWriter,
												"s", browser);

								if (PageDiffUtil.isUnixMacOrSolarisOS())
									phantomJsCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
											+ phantomJSBinPath
											+ " "
											+ getCaptureJSBinPath
											+ " "
											+ sourceUrl.toString()
											+ " "
											+ fileName
											+ " "
											+ viewPortSize
											+ " " 
											+ browser;
								else
									phantomJsCmd = phantomJSBinPath
											+ " "
											+ getCaptureJSBinPath
											+ " "
											+ sourceUrl.toString()
											+ " "
											+ fileName
											+ " "
											+ viewPortSize
											+ " " 
											+ browser + " ";
								LOGGER.info("Screen capture cmd:"
										+ phantomJsCmd);
								sourceScreenShots.add(fileName);
								takeScreenShotCmdQ.add(phantomJsCmd);
								LOGGER.info("Current TARGET URL:"
										+ targetUrl.toString());
								LOGGER.info("Current User Agent:"
										+ browser);
								fileName = PageDiffUtil.getAbsoluteTargetFileName(fileName);
								if (PageDiffUtil.isUnixMacOrSolarisOS())
									phantomJsCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
											+ phantomJSBinPath
											+ " "
											+ getCaptureJSBinPath
											+ " "
											+ targetUrl.toString()
											+ " "
											+ fileName
											+ " "
											+ viewPortSize
											+ " " + browser;
								else
									phantomJsCmd = phantomJSBinPath
											+ " "
											+ getCaptureJSBinPath
											+ " "
											+ targetUrl.toString()
											+ " "
											+ fileName
											+ " "
											+ viewPortSize
											+ " " + browser + " ";
								LOGGER.info("Screen capture cmd:"
										+ phantomJsCmd);
								targetScreenShots.add(fileName);								
								takeScreenShotCmdQ.add(phantomJsCmd);
							} else
								LOGGER.info(
										"Invalid user agent string:" + browser
										);
						}
					}

				}
				LOGGER.info("Size of takeScreenShotCmdQ:"+takeScreenShotCmdQ.size());
				Executor exec = new Executor();
				LOGGER.info("************************************* Taking screen shots ************************************");
				isTaskcompleted = exec.parseResponse(exec.processTakeSSCommands(takeScreenShotCmdQ));
				if(isTaskcompleted)
					removeFailedSS();
			} else
				LOGGER.info(
						"Either user agent string is missing or the urls are not provided");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isTaskcompleted;
	}
	
	/**
	 * This is method is responsible for resizing the screenshots
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */

	public static String resizeScreenShot(String sourceFilePath,
			String targetFilePath) {

		long sourceHeight = 0;
		long targetHeight = 0;
		long targetWidth = 0;
		long sourceWidth = 0;
		long height = 0;
		long width = 0;
		String convertCmd = null;
		File sourceFile = null;
		File targetFile = null;
		if (sourceFilePath != null && targetFilePath != null
				&& sourceFilePath.length() > 0 && targetFilePath.length() > 0) {
			sourceFile = new File(sourceFilePath);
			targetFile = new File(targetFilePath);
			sourceHeight = ImageMagicMgmt.getImageHeight(sourceFile);
			targetHeight = ImageMagicMgmt.getImageHeight(targetFile);
			targetWidth = ImageMagicMgmt.getImageWidth(targetFile);
			sourceWidth = ImageMagicMgmt.getImageWidth(sourceFile);
			try {
				if((sourceHeight != targetHeight) || (targetWidth != sourceWidth)){
		
					height=Math.min(sourceHeight, targetHeight);
					width=Math.min(sourceWidth, targetWidth);
				//if (sourceHeight > targetHeight) {
					if (PageDiffUtil.isUnixMacOrSolarisOS())
						convertCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
								+ convertBinFilePath + " "
								+ sourceFile.getAbsolutePath()
								+ " -background none -extent " + width
								+ "x" + height + " "
								+ sourceFile.getAbsolutePath()+"~"+
								PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
								+ convertBinFilePath + " "
								+ targetFile.getAbsolutePath()
								+ " -background none -extent " + width
								+ "x" + height + " "
								+ targetFile.getAbsolutePath();
					else
						convertCmd = convertBinFilePath
								+ " " + sourceFile.getAbsolutePath()
								+ " -background none -extent " + width
								+ "x" + height + " "
								+ sourceFile.getAbsolutePath()+"~"+
								convertBinFilePath
								+ " " + targetFile.getAbsolutePath()
								+ " -background none -extent " + width
								+ "x" + height + " "
								+ targetFile.getAbsolutePath();
					LOGGER.info(convertCmd);
//				} else {
//					if (PageDiffUtil.isUnixMacOrSolarisOS())
//						convertCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
//								+ convertBinFilePath + " "
//								+ targetFile.getAbsolutePath()
//								+ " -background none -extent " + width
//								+ "x" + height + " "
//								+ targetFile.getAbsolutePath();
//					else
//						convertCmd = convertBinFilePath
//								+ " " + targetFile.getAbsolutePath()
//								+ " -background none -extent " + width
//								+ "x" + height + " "
//								+ targetFile.getAbsolutePath();
//					LOGGER.log(Level.INFO, convertCmd, true);
//				}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return convertCmd;
	}

	/**
	 * Compares the images and spits out the pixel difference
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static String generateDiffImage(String sourceFilePath,
			String targetFilePath) {
		Process process = null;
		String errors = null;
		String compareCmd = null;
		File sourceFile = null;
		File targetFile = null;
		String diffFileName = PageDiffUtil
				.getAbsoluteDiffFileName(sourceFilePath);
		if (sourceFilePath != null && targetFilePath != null
				&& sourceFilePath.length() > 0 && targetFilePath.length() > 0) {
			sourceFile = new File(sourceFilePath);
			targetFile = new File(targetFilePath);

			try {
				if (PageDiffUtil.isUnixMacOrSolarisOS())
					compareCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
							+ compareBinFilePath + " "
							+ PageDiffUtil.IMAGE_FUZZINESS + " " + PageDiffUtil.HIGHLIGHT_DIFF_COLOR + " "
							+ sourceFilePath + " " + targetFilePath + " "
							+ diffFileName;
				else
					compareCmd = compareBinFilePath + " "
							+ PageDiffUtil.IMAGE_FUZZINESS + " " + PageDiffUtil.HIGHLIGHT_DIFF_COLOR + " "
							+ sourceFilePath + " " + targetFilePath + " "
							+ diffFileName;
				LOGGER.info(compareCmd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		diffImgs.add(diffFileName);
		return compareCmd;
	}

	/**
	 * Generates and combines full size images to display in gallery as end result
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @param diffFilePath
	 */
	public static void generateFullSizeCombinedImageAndThumbnails(
			String sourceFilePath, String targetFilePath, String diffFilePath) {
		String montageCmd = null;
		String mogrifyCmd = null;
		File sourceFile = null;
		File targetFile = null;
		long targetHeight = 0;
		long targetWidth = 0;
		String combinedImageFileName = null;
		String thumbnailsFileName = null;
		if (sourceFilePath != null && targetFilePath != null
				&& diffFilePath != null && sourceFilePath.length() > 0
				&& targetFilePath.length() > 0 && diffFilePath.length() > 0) {
			sourceFile = new File(sourceFilePath);
			targetFile = new File(targetFilePath);
			targetHeight = ImageMagicMgmt.getImageHeight(targetFile);
			targetWidth = ImageMagicMgmt.getImageWidth(targetFile);
			combinedImageFileName = PageDiffUtil.getCombinedFileName(
					sourceFilePath);

			try {
				if (PageDiffUtil.isUnixMacOrSolarisOS())
					montageCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
							+ montageBinFilePath + " "
							+ PageDiffUtil.IMAGE_ADJOIN + " " + " "
							+ PageDiffUtil.IMAGE_GEOMETRY + " " + targetWidth
							+ "x" + targetHeight + " "
							+ PageDiffUtil.IMAGE_FRAME + " " + sourceFilePath
							+ " " + targetFilePath + " " + diffFilePath + " "
							+ combinedImageFileName;
				else
					montageCmd = montageBinFilePath + " "
							+ PageDiffUtil.IMAGE_ADJOIN + " " + " "
							+ PageDiffUtil.IMAGE_GEOMETRY + " " + targetWidth
							+ "x" + targetHeight + " "
							+ PageDiffUtil.IMAGE_FRAME + " " + sourceFilePath
							+ " " + targetFilePath + " " + diffFilePath + " "
							+ combinedImageFileName;
				LOGGER.info(montageCmd);
				combineScreenShotCmdQ.add(montageCmd);
				combinedImgs.add(combinedImageFileName);
				if (PageDiffUtil.isUnixMacOrSolarisOS())
					mogrifyCmd = PageDiffUtil.MAC_UNX_SOL_SCRIPT_PRFX
							+ mogrifyBinFilePath
							+ " -format gif -thumbnail "
							+ PageDiffUtil.THUMB_NAIL_SIZE + " "
							+ combinedImageFileName;
				else
					mogrifyCmd = mogrifyBinFilePath
							+ " -format gif -thumbnail "
							+ PageDiffUtil.THUMB_NAIL_SIZE + " "
							+ combinedImageFileName;
				LOGGER.info(mogrifyCmd);
				thumbnailScreenShotCmdQ.add(mogrifyCmd);
				thumbnailsFileName = combinedImageFileName.replace(".png",
						".gif");
				thumbNails.add(thumbnailsFileName);
				Gallery.generateGalleryHtml(combinedImgs, thumbNails);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Removes any corrupt images
	 */
	public static void removeFailedSS(){
		LOGGER.info("************************************* Removing corrupt images ************************************");
		LOGGER.info("sourceScreenShots size:"+sourceScreenShots.size());
		LOGGER.info("targetScreenShots size:"+targetScreenShots.size());	
		Iterator<String> sourceSSItr = null;
		Iterator<String> targetSSItr = null;
		String sourceImageFile = null;
		String targetImageFile = null;
		File sourceFile = null;
		File targetFile = null;
		int sourceQueueSize=0;
		int targetQueueSize=0;
		if (sourceScreenShots != null && targetScreenShots != null
				&& sourceScreenShots.size() > 0 && targetScreenShots.size() > 0
				&& sourceScreenShots.size() == targetScreenShots.size()) {
			sourceQueueSize=sourceScreenShots.size();
			targetQueueSize=targetScreenShots.size();
			sourceSSItr = sourceScreenShots.iterator();
			targetSSItr = targetScreenShots.iterator();
			while (sourceSSItr.hasNext() && targetSSItr.hasNext()) {
				sourceImageFile = sourceSSItr.next();
				targetImageFile = targetSSItr.next();
				if(PageDiffUtil.isFileNamesMatches(sourceImageFile, targetImageFile)){
					sourceFile = new File(sourceImageFile);
					targetFile = new File(targetImageFile);
					if(!(sourceFile.exists() && targetFile.exists())){
						sourceScreenShots.remove(sourceImageFile);
						targetScreenShots.remove(targetImageFile);
					}
				}
			}
			if((sourceScreenShots.size() == sourceQueueSize) && (targetScreenShots.size() ==targetQueueSize))
				LOGGER.info("No corrupted screen shots were found");
			else
				LOGGER.info("Corrupted screen shots were found and removed");
				
		}else{
			LOGGER.warn("There are no screen shots available to process. Terminating the programme..........");
			System.exit(1);
		}
		
	}

}
