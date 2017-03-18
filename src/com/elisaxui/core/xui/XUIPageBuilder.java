package com.elisaxui.core.xui;

import java.util.ArrayList;
import java.util.HashMap;


public class XUIPageBuilder {

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

	private String getPart(String tag, String attr, int tab, HtmlPart part) {
		ArrayList<Object> partData = listPart.get(part);
		StringBuffer tabstr = new StringBuffer();
		StringBuffer tabstrInner = new StringBuffer();
		if (tab >= 0) {
			for (int i = 0; i < tab; i++) {
				tabstr.append("\t");
			}
			tabstrInner.append(tabstr).append("\t");
		}

		if (partData != null) {
			StringBuilder buf = new StringBuilder(1000);
			for (Object object : partData) {
				if (object != null)
					buf.append(tabstrInner);
				buf.append(object.toString());
			}
			if (buf.length() > 0)
				return (tag != null ? tabstr + "<" + tag + " " + attr + ">\n" : "") + buf
						+ (tag != null ? "" + tabstr + "</" + tag + ">" : "");
			else
				return "";
		} else
			return "";
	}

	public final StringBuilder getPage() {
		String template = "<!doctype html>\n" + "<html lang=\"" + getPart(null, null, -1, HtmlPart.LANG) + "\">\n"
				+ "<head>\n" + getPart(null, null, 0, HtmlPart.HEADER) +
				// " <title>Default Page Title</title>\n"+
				// " <link rel=\"shortcut icon\" href=\"favicon.ico\">\n"+
				// " <link rel=\"icon\" href=\"favicon.ico\">\n"+
				// " <link rel=\"stylesheet\" type=\"text/css\"
				// href=\"styles.css\">\n"+
				// " <script type=\"text/javascript\"
				// src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n"+
				// "<!--[if lt IE 9]>\n"+
				// " <script
				// src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n"+
				// "<![endif]-->\n"+
				"</head>\n" + getPart("body", "", 0, HtmlPart.BODY) + "\n"
				+ getPart("script", "type=\"text/javascript\"", 0, HtmlPart.SCRIPT_AFTER_BODY) + "</html>";

		return new StringBuilder(template);
	}

}
