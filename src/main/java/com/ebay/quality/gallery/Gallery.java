package com.ebay.quality.gallery;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Queue;

import org.apache.commons.io.IOUtils;

/**
 * This class generates the html report 
 * for end user for failure analysis purpose
 * @author kvikram
 *
 */
public class Gallery {

	public static void generateGalleryHtml(
			Queue<String> fullSizeImageFileNames, Queue<String> thumbnails) {
		InputStream is = Gallery.class.getResourceAsStream("/gallery.css");
		FileWriter fWriter = null;
		String thumbNailPath = null;
		String fullImagePath = null;
		BufferedWriter writer = null;
		StringBuffer html = new StringBuffer();
		StringBuffer htmlMobileDiv = new StringBuffer();
		StringBuffer htmlTabletDiv = new StringBuffer();
		StringBuffer htmlDesktopDiv = new StringBuffer();
		Iterator<String> fullSizeImgItr = fullSizeImageFileNames.iterator();
		Iterator<String> thumbnailsItr = thumbnails.iterator();
		html.append("<!DOCTYPE html>");
		html.append("<html>");
		html.append("<head>");
		html.append("<meta charset=\"US-ASCII\">");
		html.append("<title>Image Gallery</title>");
		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"gallery.css\">");
		html.append("</head>");
		html.append("<body>");

		while (fullSizeImgItr.hasNext() && thumbnailsItr.hasNext()) {
			thumbNailPath = thumbnailsItr.next();
			fullImagePath = fullSizeImgItr.next();
			if (thumbNailPath != null && fullImagePath != null
					&& thumbNailPath.length() > 0 && fullImagePath.length() > 0
					&& thumbNailPath.contains("/mobile")
					&& fullImagePath.contains("/mobile")) {
				htmlMobileDiv.append("<div class=\"img\">");
				htmlMobileDiv
						.append("<a target=\"_blank\" href=\""
								+ fullImagePath
								+ "\"><img src=\""
								+ thumbNailPath
								+ "\" alt=\"mobile\" width=\"100\" height=\"150\"></a>");
				htmlMobileDiv.append("</div>");
			} else if (thumbNailPath != null && fullImagePath != null
					&& thumbNailPath.length() > 0 && fullImagePath.length() > 0
					&& thumbNailPath.contains("/tablet")
					&& fullImagePath.contains("/tablet")) {
				htmlTabletDiv.append("<div class=\"img\">");
				htmlTabletDiv
						.append("<a target=\"_blank\" href=\""
								+ fullImagePath
								+ "\"><img src=\""
								+ thumbNailPath
								+ "\" alt=\"tablet\" width=\"100\" height=\"150\"></a>");
				htmlTabletDiv.append("</div>");
			} else if (thumbNailPath != null && fullImagePath != null
					&& thumbNailPath.length() > 0 && fullImagePath.length() > 0
					&& thumbNailPath.contains("/desktop")
					&& fullImagePath.contains("/desktop")) {
				htmlDesktopDiv.append("<div class=\"img\">");
				htmlDesktopDiv
						.append("<a target=\"_blank\" href=\""
								+ fullImagePath
								+ "\"><img src=\""
								+ thumbNailPath
								+ "\" alt=\"desktop\" width=\"100\" height=\"150\"></a>");
				htmlDesktopDiv.append("</div>");
			}
		}
		html.append("<h3>Mobile</h3>");
		html.append("<div id=\"container\">");
		html.append(htmlMobileDiv);
		html.append("</div>");
		html.append("<h3>Tablet</h3>");
		html.append("<div id=\"container\">");
		html.append(htmlTabletDiv);
		html.append("</div>");
		html.append("<h3>Desktop</h3>");
		html.append("<div id=\"container\">");
		html.append(htmlDesktopDiv);
		html.append("</div>");
		html.append("</body>");
		html.append("</html>");
		try {
			OutputStream out;
			String captureSsJSFileName = "gallery.css";
			out = new FileOutputStream(captureSsJSFileName);
			IOUtils.copy(is, out);
			out.close();
			is.close();
			fWriter = new FileWriter("gallery.html");
			writer = new BufferedWriter(fWriter);
			writer.write(html.toString());

			writer.close();
		} catch (Exception e) {

		}
	}

}
