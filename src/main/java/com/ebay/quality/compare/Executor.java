package com.ebay.quality.compare;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.ebay.quality.config.PageDiffClineArgs;
import com.ebay.quality.config.PageDiffRuntimeArgs;
import com.ebay.quality.utils.PageDiffUtil;


public class Executor {
	
	private static ExecutorService executor = null;
	private final static Logger LOGGER = Logger.getLogger(Executor.class);
	public static void startVivid(String[] args){
		PageDiffUtil.printProjectName();
		for(String arg:args){
			LOGGER.info(arg);
		}
		PageDiffClineArgs runtimeargs=PageDiffRuntimeArgs.getRuntimeArgs(args);
		executor = ExecutionServiceImpl.getexecutionSrvcImpl(runtimeargs.threadCount).getExecutor();		
		boolean isAllScreenShotsTaken = ScreenShotComperator.prepareScreenShots(runtimeargs);
		if(isAllScreenShotsTaken)
			ScreenShotComperator.processScreenShots();
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI("file://"+System.getProperty("user.dir").replaceAll("\\\\", "/")+PageDiffUtil.PATH_SEPARATOR+"gallery.html"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(!executor.isShutdown()){
				try {
					executor.shutdown();
					executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}
		//System.exit(0);
	}
	
public List<FutureTask<String>> execute (List<Callable<String>> workerQ){
	List<FutureTask<String>> responseLst = new ArrayList<FutureTask<String>>();
	FutureTask<String> workerTask =null;
	if(workerQ!=null && workerQ.size()>0){
		Iterator<Callable<String>> workerItr = workerQ.iterator();
		while(workerItr.hasNext()){
			workerTask=new FutureTask<String>(workerItr.next());
			executor.submit(workerTask);
			responseLst.add(workerTask);
		}
		
	}
	return responseLst;
	
}

public List<FutureTask<String>> processTakeSSCommands(Queue<String> commandsQueue){
	PhantomJSWorker phantomWorker =null;
	List<Callable<String>> takeSSWorkerQ=new ArrayList<Callable<String>>();
	if(commandsQueue!=null && commandsQueue.size()>0){
		Iterator<String> cmdItr = commandsQueue.iterator();
		while(cmdItr.hasNext()){
			phantomWorker= new PhantomJSWorker(cmdItr.next());
			takeSSWorkerQ.add(phantomWorker);
		}
	}
	LOGGER.info("Size of takeSSWorkerQ:"+takeSSWorkerQ.size());
	return execute(takeSSWorkerQ);
}

public List<FutureTask<String>> processImageMagickCommands(Queue<String> commandsQueue){
	ImageMagickWorker imageMagickWrkr =null;
	List<Callable<String>> imageMagickWrkrQ=new ArrayList<Callable<String>>();
	if(commandsQueue!=null && commandsQueue.size()>0){
		Iterator<String> cmdItr = commandsQueue.iterator();
		while(cmdItr.hasNext()){
			imageMagickWrkr= new ImageMagickWorker(cmdItr.next());
			imageMagickWrkrQ.add(imageMagickWrkr);
		}
	}
	return execute(imageMagickWrkrQ);
}


public boolean parseResponse(List<FutureTask<String>> responses){
	FutureTask<String> taskRes =null;
	boolean isAllTaskCompleted=false;
	int taskCompleted=0;
	int taskSubmitted=0;
	if(responses!=null && responses.size()>0){
		taskSubmitted=responses.size();
		LOGGER.info("Total number of task submitted:"+taskSubmitted);
		Iterator<FutureTask<String>> resItr = responses.iterator();
		
		try {
		while(resItr.hasNext()){
			taskRes=resItr.next();	
			while(taskRes.get()!=null && taskRes.get().length()>0){
				LOGGER.info("Completed "+ ++taskCompleted+" out of "+taskSubmitted + " submitted tasks.");
				break;
			}
			
		}
		if(taskCompleted==taskSubmitted)
			isAllTaskCompleted = true;
		LOGGER.info("Has all submitted task completed?:" +isAllTaskCompleted);
		} catch (InterruptedException ie) {
					// TODO Auto-generated catch block
			LOGGER.info(ie.getMessage());
					ie.printStackTrace();
		} catch (ExecutionException ee) {
					// TODO Auto-generated catch block
					LOGGER.info(ee.getMessage());
					ee.printStackTrace();
					
		} catch (Exception ue) {
			// TODO Auto-generated catch block
			LOGGER.info(ue.getMessage());
			ue.printStackTrace();
}
			
	}	
	return isAllTaskCompleted;
	}

}
