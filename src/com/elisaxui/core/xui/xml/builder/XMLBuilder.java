package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xml.XMLPart;

/**
 * 
 * @author Bureau
 *
 */
public class XMLBuilder {

	String id; // identifiant du bloc

	StringBuilder content;
	StringBuilder afterContent;
	boolean after = false;

	boolean isString = false; // creer un string '<div>'
	boolean isTemplate = false; // e("div",[])
	JSContent jsTemplate = null;

	/**
	 * @return the js
	 */
	public final JSContent getJSContent() {
		return jsTemplate;
	}

	/**
	 * @return the isTemplate
	 */
	public final boolean isTemplate() {
		return isTemplate;
	}

	/**
	 * @param isTemplate
	 *            the isTemplate to set
	 */
	public final XMLBuilder setModeTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
		jsTemplate = new JSContent();
		return this;
	}

	public boolean isModeString() {
		return isString;
	}

	public XMLBuilder setModeString(boolean isJS) {
		this.isString = isJS;
		return this;
	}

	public XMLBuilder(String id, StringBuilder content, StringBuilder afterContent) {
		super();
		this.content = content;
		this.afterContent = afterContent;
		this.id = id;
	}

	public static XMLElement createElement(Object name, Object... inner) {
		return new XMLElement(name, inner);
	}

	public static XMLAttr createAttr(Object name, Object value) {
		return new XMLAttr(name, value);
	}

	public static XMLPartElement createPart(XMLPart part, Object... inner) {
		XMLPartElement t = new XMLPartElement(part, inner);
		part.doContent(XUIFactoryXHtml.getXMLRoot());
		return t;
	}

	public static Handle createHandle(String name) {
		return new Handle(name);
	}

	/**
	 * ajoute dans le flux : ici une chaine
	 * 
	 * @param v
	 */
	public void addContentOnTarget(Object v) {

		if (isTemplate)
			this.getJSContent().getListElem().add(v);
		else {
			if (afterContent == null) {
				after = false;
			}
			(after ? afterContent : content).append(v);
		}
	}

	public static class Handle {

		private String name;
		private Object ifExistAdd;

		/**
		 * @return the ifExistAdd
		 */
		public final Object getIfExistAdd() {
			return ifExistAdd;
		}

		/**
		 * @param ifExistAdd the ifExistAdd to set
		 */
		public final void setIfExistAdd(Object ifExistAdd) {
			this.ifExistAdd = ifExistAdd;
		}

		public Handle(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
