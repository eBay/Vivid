package com.ebay.quality.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.Parameter;

//@Parameters(separators = "#")
public class PageDiffClineArgs {

	@Parameter(names = "-readScreenShotsFrom", description = "Location of the stored screen shots on the file system. If not provided default will be taken")
	public String readScreenShotsFrom = null;

	@Parameter(names = "-writeScreenShotsAt", description = "Location on the file system where screen shots will be stored. If not provided default will be taken")
	public String writeScreenShotsAt = null;

	@Parameter(names = "-urlsToComapre", description = "Pair of urls | seperated and two sets of pair of urls # seperated")
	public String[] urlsToComapre = null;

	@Parameter(names = "-userAgents", description = "User agents to decide the type of device the url should render upon. # seperated list of user agents")
	public String[] userAgents = null;

	@Parameter(names = "-parsedUrlPairList", description = "List of pair of urls | seperated", hidden = true)
	public List<Map> parsedUrlPairList = new ArrayList<Map>();

	@Parameter(names = "-parsedUserAgentList", description = "List of user agents", hidden = true)
	public List<String> parsedUserAgentList = new ArrayList<String>();

	@Parameter(names = "-propertyFilePath", description = "Path of the file which contains url and user agent string")
	public String propertyFilePath = null;

	@Parameter(names = "-mobileResolution", description = "View port size for mobile devices")
	public String mobileResolution = null;

	@Parameter(names = "-tabletResolution", description = "View port size for tablet devices")
	public String tabletResolution = null;

	@Parameter(names = "-desktopResolution", description = "View port size for desktop")
	public String desktopResolution = null;
	
	@Parameter(names = "-threadCount", description = "Number of threads the application will run on")
	public int threadCount = 0;

}
