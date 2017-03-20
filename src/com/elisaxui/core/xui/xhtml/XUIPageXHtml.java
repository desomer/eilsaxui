package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;
import java.util.HashMap;

import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLBuilder;
import com.elisaxui.core.xui.xml.XMLPart;

public class XUIPageXHtml extends XMLPart {

	public enum HtmlPart {
		LANG, HEADER, BODY, SCRIPT_AFTER_BODY
	}

	private HashMap<HtmlPart, ArrayList<Object>> listPart = new HashMap<HtmlPart, ArrayList<Object>>();

	public void addPart(HtmlPart part, Object value) {
		ArrayList<Object> partData = listPart.get(part);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(part, partData);
		}
		partData.add(value);

	}

//	private String getPart(String tag, String attr, int tab, HtmlPart part) {
//		ArrayList<Object> partData = listPart.get(part);
//		StringBuffer tabstr = new StringBuffer();
//		StringBuffer tabstrInner = new StringBuffer();
//		if (tab >= 0) {
//			for (int i = 0; i < tab; i++) {
//				tabstr.append("\t");
//			}
//			tabstrInner.append(tabstr).append("\t");
//		}
//
//		if (partData != null) {
//			StringBuilder buf = new StringBuilder(1000);
//			for (Object object : partData) {
//				if (object != null)
//					buf.append(tabstrInner);
//				buf.append(object.toString());
//			}
//			if (buf.length() > 0)
//				return (tag != null ? tabstr + "<" + tag + " " + attr + ">" : "") + buf
//						+ (tag != null ? "" + tabstr + "\n</" + tag + ">" : "");
//			else
//				return "";
//		} else
//			return "";
//	}

	
//	String template = "<!doctype html>\n" + "<html lang=\"" +  + "\">\n"
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
//	// "<![endif]-->\n"+
//	"</head>\n" + getPart("body", "", -1, HtmlPart.BODY)
//	+ getPart(null, null,-1, HtmlPart.SCRIPT_AFTER_BODY)+ "\n</html>";

	private StringBuilder getPart(HtmlPart part) {
		ArrayList<Object> partData = listPart.get(part);
		StringBuilder buf = new StringBuilder(1000);	
		if (partData != null) {

			for (Object object : partData) {
				if (object != null)
					buf.append(object.toString());
			}
		}
		return buf;
	}
	
	public final StringBuilder getPage() {
		
		StringBuilder header =  getPart(HtmlPart.HEADER);
		StringBuilder body =  getPart(HtmlPart.BODY);
		
		Element p = xElement("html", xAttr("lang", xTxt(getPart(HtmlPart.LANG))),
				xElement("head", 
						xElement("meta", xAttr("charset", xTxt("utf-8"))),
						xElement("meta", xAttr("name", xTxt("viewport")), xAttr("content", xTxt("width=device-width, initial-scale=1.0, shrink-to-fit=no"))),
						(header.length()==0 ? "": xElement(null, header))),
				xElement("body", xElement(null, body)),
				xElement(null, getPart(HtmlPart.SCRIPT_AFTER_BODY))
				);
		
		StringBuilder buf = new StringBuilder(1000);
		p.toXML(new XMLBuilder("page", buf, null));
		return buf;
		
	}

}
