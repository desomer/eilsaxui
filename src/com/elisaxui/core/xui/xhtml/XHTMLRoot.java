package com.elisaxui.core.xui.xhtml;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.elisaxui.core.xui.XUILaucher;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.XMLTarget;
import com.elisaxui.core.xui.xml.builder.XMLTarget.ITargetRoot;
import com.elisaxui.helper.ClassLoaderHelper;
import com.elisaxui.helper.ClassLoaderHelper.FileEntry;

public class XHTMLRoot extends XHTMLPart {

	public static final class HEADER extends XMLTarget implements ITargetRoot {
	};

	public static final class BODY extends XMLTarget implements ITargetRoot {
	};

	public static final class AFTER_BODY extends XMLTarget implements ITargetRoot {
	};

	private Object lang;

	public Object getLang() {
		return lang;
	}

	@Deprecated
	/** gerer par properties */
	public void setLang(Object lang) {
		this.lang = lang;
	}
;
	
	static int nbChangement = 0;
	static long lastOlderFile = 0;
	
	@xTarget(XMLPart.CONTENT.class)
	public Element xGetContent() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		long olderFile = 0;
		try {
			Iterable<FileEntry> list = ClassLoaderHelper.listFilesRelativeToClass(XUILaucher.class,	"com"); ///elisaxui/core/xui/xml
			for (FileEntry fileEntry : list) {
				long lm = fileEntry.file.lastModified();
				//Date d = new Date();
				//System.out.println(fileEntry.file.getName() + " " + d);
				if (lm > olderFile)
					olderFile = lm;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lastOlderFile!=olderFile)
		{
			lastOlderFile=olderFile;
			nbChangement++;
		}
		
		Date input = new Date(olderFile);
		Instant instant = input.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		LocalDateTime dateBuild = zdt.toLocalDateTime();
		
		String formatDateTimeBuild =   dateBuild.format(formatter);
		String formatDateTimeNow = now.format(formatter);

		StringBuilder textbody = new StringBuilder(1000);
		StringBuilder textAfterbody = new StringBuilder(1000);
		//StringBuilder textAfterbody2 = new StringBuilder(1000);
		
		xListElement(getListElement(BODY.class)).setNbInitialTab(1)
				.toXML(new XMLBuilder("page", textbody, textAfterbody));

		xListElement(getListElement(AFTER_BODY.class)).setNbInitialTab(0)
				.toXML(new XMLBuilder("page", textAfterbody, null));
		
		ArrayList<Element> header = getListElement(HEADER.class);
		Collections.reverse(header);

		return xElement("html", xAttr("lang", xTxt(lang)),
				xListElement("\n"),
				xComment("version 1.0.0" +nbChangement , "generated at " + formatDateTimeNow  , "build at " + formatDateTimeBuild),
				xElement("head", xElement("meta", xAttr("charset", xTxt("utf-8"))),
						xElement("meta", xAttr("name", xTxt("mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xElement("meta", xAttr("name", xTxt("apple-mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xElement("meta", xAttr("name", xTxt("application-name")), xAttr("content", xTxt("Elisa"))),
						xElement("meta", xAttr("name", xTxt("viewport")),
								xAttr("content", xTxt("width=device-width, initial-scale=1.0, shrink-to-fit=no, user-scalable=no"))),
						header
						),
				xElement("body", textbody),
				xListElement(textAfterbody)
				);
	}

	// + "<head>\n" + getPart(null, null, 0, HtmlPart.HEADER) +
	// // " <title>Default Page Title</title>\n"+
	// // " <link rel=\"shortcut icon\" href=\"favicon.ico\">\n"+
	// // " <link rel=\"icon\" href=\"favicon.ico\">\n"+
	// // " <link rel=\"stylesheet\" type=\"text/css\"
	// // href=\"styles.css\">\n"+
	// // " <script type=\"text/javascript\"
	// //
	// src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n"+
	// // "<!--[if lt IE 9]>\n"+
	// // " <script
	// //
	// src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n"+

}
