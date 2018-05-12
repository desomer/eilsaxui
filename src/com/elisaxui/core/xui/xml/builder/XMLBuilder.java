package com.elisaxui.core.xui.xml.builder;

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
	boolean isResource = false; // pas de XML dans un fichier JS ou CSS
	
	// contenu js du template
	JSContent jsTemplate = null;
	
	/**
	 * @return the isResource
	 */
	public final boolean isResource() {
		return isResource;
	}

	/**
	 * @param isResource the isResource to set
	 */
	public final XMLBuilder setModeResource(boolean isResource) {
		this.isResource = isResource;
		return this;
	}

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
		if (isTemplate)
			jsTemplate = new JSContent();
		else
			jsTemplate = null;
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

	/**
	 * @return the content
	 */
	public final StringBuilder getContent() {
		return content;
	}

	public static XMLElement createElement(Object name, Object... inner) {
		return new XMLElement(name, inner);
	}

	public static XMLAttr createAttr(Object name, Object value) {
		return new XMLAttr(name, value);
	}

	public static XMLPartElement createPart(XMLPart part, Object... child) {
		XMLPartElement t = new XMLPartElement(part, child);
		part.doContent();
		return t;
	}

	public static XMLHandle createHandle(String name) {
		return new XMLHandle(name);
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

	public static class XMLHandle {

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

		public XMLHandle(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
