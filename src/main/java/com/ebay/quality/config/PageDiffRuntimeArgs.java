package com.ebay.quality.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.beust.jcommander.JCommander;
import com.ebay.quality.io.ScreenShotWriter;
import com.ebay.quality.io.ScreenShotWriterImpl;
import com.ebay.quality.utils.PageDiffUtil;

public class PageDiffRuntimeArgs {

	static Logger LOGGER = Logger
			.getLogger(PageDiffRuntimeArgs.class);

	public static PageDiffClineArgs configs = new PageDiffClineArgs();
	private static ScreenShotWriter scWriter = new ScreenShotWriterImpl();

	public PageDiffRuntimeArgs() {
		// initArgs();
	}

	public static void initArgs(String[] cliArgs) {
		JCommander commandLineParams = new JCommander(configs);
		commandLineParams.parse(cliArgs);
		if (configs.propertyFilePath == null
				|| configs.propertyFilePath.length() == 0) {
			LOGGER.warn(
					"External properties file was not provided. Loading properties from resource directory........."
					);
			loadRuntimePropertiesFromResources();
		} else {
			LOGGER.warn(
					"Loading properties from external properties file........"
					);
			loadRuntimePropertiesFromPropertyFile(configs.propertyFilePath);
		}
		configs.parsedUrlPairList = UrlConverter
				.parseUrl(configs.urlsToComapre);
		configs.parsedUserAgentList = UserAgentConverter
				.getUserAgentList(configs.userAgents);

		if (configs.writeScreenShotsAt == null
				|| configs.writeScreenShotsAt.length() == 0) {
			configs.writeScreenShotsAt = System.getProperty("user.home")+PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.PATH_SUFFIX
					+ PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.getTimeStamp();
//			configs.writeScreenShotsAt = System.getProperty("user.home")
//					+ PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.PATH_SUFFIX
//					+ PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.getTimeStamp();
		}

		if (configs.readScreenShotsFrom == null
				|| configs.readScreenShotsFrom.length() == 0) {
			configs.readScreenShotsFrom = System.getProperty("user.home")
					+ PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.PATH_SUFFIX;
		}

		if (configs.mobileResolution == null
				|| configs.mobileResolution.length() == 0) {
			configs.mobileResolution = "600px*4000px";
		}

		if (configs.tabletResolution == null
				|| configs.tabletResolution.length() == 0) {
			configs.tabletResolution = "768px*4000px";
		}

		if (configs.desktopResolution == null
				|| configs.desktopResolution.length() == 0) {
			configs.desktopResolution = "1920px*5500px";
		}
		scWriter.createScreenShotDirs(configs);
	}

	public static PageDiffClineArgs getRuntimeArgs(String[] args) {
		initArgs(args);
		return configs;
	}

	public static PropertiesConfiguration loadRuntimePropertiesFromPropertyFile(
			String propertyFilePath) {

		PropertiesConfiguration prop = new PropertiesConfiguration();
		InputStream input = null;

		try {
			
			input = new FileInputStream(propertyFilePath);

//			input = PageDiffRuntimeArgs.class
//					.getResourceAsStream(propertyFilePath);
			if (input.available()>0)
				prop.load(input);
			else {
				LOGGER.info(
						
						"Can't read properties file. Terminating the programme....."
						);
				System.exit(1);
			}
			populateRuntimeArgs(prop);
			LOGGER.info( prop.getStringArray("url").toString());
			LOGGER.info(prop.getStringArray("userAgent").toString()
					);
			LOGGER.info(prop.getString("writeScreenShotsAt"));
		} catch (ConfigurationException e) {
			LOGGER.info(
					"Something went wrong while loading the properties. Terminating the programme.....");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;

	}

	public static PropertiesConfiguration loadRuntimePropertiesFromResources() {

		PropertiesConfiguration prop = new PropertiesConfiguration();
		InputStream input = null;

		try {

			input = PageDiffRuntimeArgs.class
					.getResourceAsStream("/config.properties");

			// load a properties file
			if (input != null)
				prop.load(input);
			else {
				LOGGER.info(
						
						"Can't read properties file. Terminating the programme....."
						);
				System.exit(1);
			}
			populateRuntimeArgs(prop);
			LOGGER.info(
					
					"**********************************************************************"
					);
			LOGGER.info(
					"Running programme with following parameters");
			LOGGER.info( "URLS:" + prop.getProperty("url"));
			LOGGER.info(
					"User agent strings:" + prop.getProperty("userAgent"));
			LOGGER.info(
					
					"File location to save screenshots:"
							+ prop.getProperty("writeScreenShotsAt"));
			LOGGER.info(
					
					"**********************************************************************"
					);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;

	}

	public static void populateRuntimeArgs(
			PropertiesConfiguration runtimeProperties) {
		if (runtimeProperties != null && !runtimeProperties.isEmpty()) {
			configs.urlsToComapre = runtimeProperties.getStringArray("url");
			configs.userAgents = runtimeProperties.getStringArray("userAgent");
			configs.writeScreenShotsAt = (runtimeProperties
					.getProperty("writeScreenShotsAt") == null) ? ""
					: runtimeProperties.getProperty("writeScreenShotsAt")
							.toString()+PageDiffUtil.PATH_SEPARATOR + PageDiffUtil.getTimeStamp();;
			configs.readScreenShotsFrom = (runtimeProperties
					.getProperty("readScreenShotsFrom") == null) ? ""
					: runtimeProperties.getProperty("readScreenShotsFrom")
							.toString();
			configs.mobileResolution = runtimeProperties
					.getString("mobileResolution");
			configs.tabletResolution = runtimeProperties
					.getString("tabletResolution");
			configs.desktopResolution = runtimeProperties
					.getString("desktopResolution");
			configs.threadCount = runtimeProperties.getString("threadCount")==null?0:Integer.parseInt(runtimeProperties.getString("threadCount"));
			LOGGER.info(
					
					"**********************************************************************"
					);
			LOGGER.info("URLS:" + configs.urlsToComapre.toString()
					);
			LOGGER.info("User agent strings:" + configs.userAgents
					);
			LOGGER.info("File location to save screenshots:"
					+ configs.writeScreenShotsAt);
			LOGGER.info( "View port size for mobile device:"
					+ configs.mobileResolution);
			LOGGER.info("View port size for tablet device:"
					+ configs.tabletResolution);
			LOGGER.info( "View port size for desktop device:"
					+ configs.desktopResolution);
			LOGGER.info(
					
					"**********************************************************************"
					);
		} else {
			LOGGER.info(
				
					"Something went wrong while parsing the properties file. Terminating the programme............"
					);
			System.exit(1);
		}
	}

}
