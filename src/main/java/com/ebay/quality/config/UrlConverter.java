package com.ebay.quality.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class UrlConverter {
	static Logger LOGGER = Logger.getLogger(UrlConverter.class);

	public static List<Map> parseUrl(String[] urlString) {
		LOGGER.info( "Following urls were entered:" + urlString);
		Map<String, URL> urlMap = null;
		List<Map> parsedurls = new ArrayList<Map>();
		if (urlString != null && urlString.length > 0) {
			for (String urlPair : urlString) {
				String[] urls = urlPair.split("\\|");
				if (urls.length == 2) {
					urlMap = new HashMap<String, URL>();
					try {
						urlMap.put("Source", new URL(urls[0]));
						urlMap.put("Target", new URL(urls[1]));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					LOGGER.info(
							"There must be two urls to compare the screen shot");
				}
				parsedurls.add(urlMap);
			}
			LOGGER.info("Final list of URLs contains following url:"
					+ parsedurls.toString());

		}
		return parsedurls;
	}

}
