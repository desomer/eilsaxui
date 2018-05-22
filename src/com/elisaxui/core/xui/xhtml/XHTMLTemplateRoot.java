package com.elisaxui.core.xui.xhtml;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;


public class XHTMLTemplateRoot extends XHTMLTemplate {

	/**
	 * @author Bureau
	 *
	 */
	public static final class XMLElementComparator implements Comparator<XMLElement> {
		@Override
		public int compare(XMLElement arg0, XMLElement arg1) {
			return (int)((arg0.getPriority()*XMLPart.PRECISION)-(arg1.getPriority()*XMLPart.PRECISION));
		}
	}

	
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
	
	
	@xTarget(CONTENT.class)
	@xResource
	public XMLElement xGetContent() {
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime dateBuild = XUIFactory.changeMgr.dateBuild;
		
		String formatDateTimeBuild =   dateBuild.format(formatter);
		String formatDateTimeNow = now.format(formatter);

		StringBuilder textbody = new StringBuilder(1000);
		StringBuilder textAfterbody = new StringBuilder(1000);
		
		List<XMLElement> body = getListElementFromTarget(BODY.class);
		List<XMLElement> afterBody = getListElementFromTarget(AFTER_BODY.class);
		List<XMLElement> header = getListElementFromTarget(HEADER.class);
		
		Collections.sort(header, new XMLElementComparator() );
		Collections.sort(body, new XMLElementComparator() );
		Collections.sort(afterBody, new XMLElementComparator() );
		
		xListNode(body).getXMLElementTabbed(2)
				.toXML(new XMLBuilder("page", textbody, textAfterbody));

		xListNode(afterBody).getXMLElementTabbed(1)
				.toXML(new XMLBuilder("page", textAfterbody, null));
		
		XMLElement timeGenerated = null;
		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().getData().isTimeGenerated())
			timeGenerated = xListNode("\n" ,xComment("version 1.0.0" + XUIFactory.changeMgr.nbChangement , "generated at " + formatDateTimeNow  , "build at " + formatDateTimeBuild));
			
		return xNode("html", xAttr("lang", xTxt(lang)),
				timeGenerated, 
				xNode("head", xNode("meta", xAttr("charset", xTxt("utf-8"))),
						xNode("meta", xAttr("name", xTxt("mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xNode("meta", xAttr("name", xTxt("apple-mobile-web-app-capable")), xAttr("content", xTxt("yes"))),
						xNode("meta", xAttr("name", xTxt("application-name")), xAttr("content", xTxt("Elisa"))),
						xNode("meta", xAttr("name", xTxt("viewport")),
								xAttr("content", xTxt("width=device-width, initial-scale=1.0, shrink-to-fit=no, user-scalable=no"))),
						header
						),
				xNode("body", textbody),
				xListNode(textAfterbody)
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
