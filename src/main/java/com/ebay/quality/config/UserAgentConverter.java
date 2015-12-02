package com.ebay.quality.config;

import java.util.ArrayList;
import java.util.List;

public class UserAgentConverter {
	
	public static List<String> getUserAgentList(String[] userAgentString){
		List<String> parsedUserAgent = new ArrayList<String>();
		if(userAgentString!=null && userAgentString.length>0){
			
			
				for(String userAgent: userAgentString){
					parsedUserAgent.add(userAgent);
				}
			}
		
		return parsedUserAgent;
	}
}
