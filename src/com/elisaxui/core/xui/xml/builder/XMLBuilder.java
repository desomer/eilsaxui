package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLPart;

/**
 * 
 * sortir la gestion des tabulations (utiliser par le JSBuilder)
 * 
 * @author Bureau
 *
 */
public class XMLBuilder  {

	String id; // identifiant du bloc
	StringBuilder content;
	StringBuilder afterContent;
	boolean after = false;
	boolean isJS = false;

	public boolean isJS() {
		return isJS;
	}

	public XMLBuilder setJS(boolean isJS) {
		this.isJS = isJS;
		return this;
	}

	public XMLBuilder(String id, StringBuilder content, StringBuilder afterContent) {
		super();
		this.content = content;
		this.afterContent = afterContent;
		this.id = id;
	}

	public static XMLElement createElement(Object name, Object... inner) {
		XMLElement t = new XMLElement(name, inner);
		return t;
	}

	public static XMLAttr createAttr(Object name, Object value) {
		XMLAttr t = new XMLAttr(name, value);
		return t;
	}

	public static XMLPartElement createPart(XMLPart part, Object... inner) {
		XMLPartElement t = new XMLPartElement(part, inner);
		part.doContent(XUIFactoryXHtml.getXMLRoot());
		return t;
	}

	public static Handle createHandle(String name) {
		Handle t = new Handle(name);
		return t;
	}

	/**
	 * ajoute dans le flux : ici une chaine
	 * 
	 * @param v
	 */
	public void addContent(Object v) {
		if (afterContent==null)
		{
			//ErrorNotificafionMgr.doError(" doit etre ajouter dans un @xTarget(CONTENT)" , null);
			after= false;
		}
		(after ? afterContent : content).append(v);
	}

	public static class Handle {
		private String name;

		public Handle(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
