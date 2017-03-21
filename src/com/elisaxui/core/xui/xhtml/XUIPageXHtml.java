package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.xml.ITarget;
import com.elisaxui.core.xui.xml.XMLBuilder;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.Content;

public class XUIPageXHtml extends XMLPart {

	public enum HtmlPart implements ITarget  {
		HEADER, BODY, SCRIPT_AFTER_BODY
	}

	private Object lang;
	public Object getLang() {
		return lang;
	}

	public void setLang(Object lang) {
		this.lang = lang;
	}
		
	@Content
	public Element xGetContent() {
		
		StringBuilder textbody = new StringBuilder(1000);
		StringBuilder textAfterbody = new StringBuilder(1000);
		
		xListElement(getPart(HtmlPart.BODY))
			.setNbInitialTab(1)
			.toXML(new XMLBuilder("page", textbody, textAfterbody));
		
		xListElement(getPart(HtmlPart.SCRIPT_AFTER_BODY))
			.setNbInitialTab(0)
			.toXML(new XMLBuilder("page", textAfterbody, null));
		
		return xElement("html", xAttr("lang", xTxt(lang)),
				xElement("head", 
						xElement("meta", xAttr("charset", xTxt("utf-8"))),
						xElement("meta", xAttr("name", xTxt("viewport")), xAttr("content", xTxt("width=device-width, initial-scale=1.0, shrink-to-fit=no"))),
						getPart(HtmlPart.HEADER)),
				xElement("body", textbody),
				xListElement(textAfterbody)  /* pas de xTxt car on veut bloc d'une tabulation */
				);
		
	}	
	
	
//	+ "<head>\n" + getPart(null, null, 0, HtmlPart.HEADER) +
//	// " <title>Default Page Title</title>\n"+
//	// " <link rel=\"shortcut icon\" href=\"favicon.ico\">\n"+
//	// " <link rel=\"icon\" href=\"favicon.ico\">\n"+
//	// " <link rel=\"stylesheet\" type=\"text/css\"
//	// href=\"styles.css\">\n"+
//	// " <script type=\"text/javascript\"
//	// src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n"+
//	// "<!--[if lt IE 9]>\n"+
//	// " <script
//	// src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n"+
	
}
