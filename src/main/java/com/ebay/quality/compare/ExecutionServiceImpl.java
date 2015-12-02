package com.ebay.quality.compare;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class ExecutionServiceImpl {
	private final static Logger LOGGER = Logger.getLogger(ExecutionServiceImpl.class);
	private static int NUMBER_OF_THREADS =5;
	private static ExecutorService executor=null;
	

	    private static class ExecutionServiceImplLoader {
	        private static final ExecutionServiceImpl executionSrvcImpl = new ExecutionServiceImpl();
	    }

	    private ExecutionServiceImpl() {
	        if (ExecutionServiceImplLoader.executionSrvcImpl != null) {
	            throw new IllegalStateException("Already instantiated");
	        }
	    }

	    public static ExecutionServiceImpl getexecutionSrvcImpl(int threadCount) {
	    	ExecutionServiceImpl executionSrvcImpl=ExecutionServiceImplLoader.executionSrvcImpl;
	    	if(threadCount>0)
	    		NUMBER_OF_THREADS=threadCount;
	    	LOGGER.info("Number of application threads:"+NUMBER_OF_THREADS);
	    		executor=Executors.newFixedThreadPool(NUMBER_OF_THREADS);
	    	return executionSrvcImpl;
	    }

	    public ExecutorService getExecutor(){
	    	return executor;
	    }
	    
		public void shutdownExecutor() {
			executor.shutdown();			
		}

}
