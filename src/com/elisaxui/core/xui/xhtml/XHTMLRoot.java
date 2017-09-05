package com.elisaxui.core.xui.xhtml;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.builder.XMLTarget;
import com.elisaxui.core.xui.xml.builder.XMLTarget.ITargetRoot;

public class XHTMLRoot extends XHTMLPart {

	/**
	 * @author Bureau
	 *
	 */
	private final class XMLElementComparator implements Comparator<XMLElement> {
		@Override
		public int compare(XMLElement arg0, XMLElement arg1) {
			return arg0.getPriority()-arg1.getPriority();
		}
	}

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
	
	
	@xTarget(XMLPart.CONTENT.class)
	public XMLElement xGetContent() {
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime dateBuild = XHTMLAppBuilder.dateBuild;
		
		String formatDateTimeBuild =   dateBuild.format(formatter);
		String formatDateTimeNow = now.format(formatter);

		StringBuilder textbody = new StringBuilder(1000);
		StringBuilder textAfterbody = new StringBuilder(1000);
		//StringBuilder textAfterbody2 = new StringBuilder(1000);
		
		ArrayList<XMLElement> body = getListElement(BODY.class);
		ArrayList<XMLElement> afterBody = getListElement(AFTER_BODY.class);
		ArrayList<XMLElement> header = getListElement(HEADER.class);
		
		Collections.sort(header, new XMLElementComparator() );
		Collections.sort(body, new XMLElementComparator() );
		Collections.sort(afterBody, new XMLElementComparator() );
		
		xListElement(body).getXMLElementTabbed(1)
				.toXML(new XMLBuilder("page", textbody, textAfterbody));

		xListElement(afterBody).getXMLElementTabbed(0)
				.toXML(new XMLBuilder("page", textAfterbody, null));
		
	//	Collections.reverse(header);

		return xElement("html", xAttr("lang", xTxt(lang)),
				xListElement("\n"),
				xComment("version 1.0.0" + XHTMLAppBuilder.nbChangement , "generated at " + formatDateTimeNow  , "build at " + formatDateTimeBuild),
				xElement("head", xElement("meta", xAttr("charset", xTxt("utf-8"))),
						xElement("meta", xAttr("name", xTxt("mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xElement("meta", xAttr("name", xTxt("apple-mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xElement("meta", xAttr("name", xTxt("application-name")), xAttr("content", xTxt("Elisa"))),
						xElement("meta", xAttr("name", xTxt("viewport")),
								xAttr("content", xTxt("width=device-width, initial-scale=1.0, shrink-to-fit=\"no\", user-scalable=\"no\""))),
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
