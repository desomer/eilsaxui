/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

/**
 * un attribut XML
 * 
 * @author Bureau
 *
 */
public class XMLAttr implements IXMLBuilder {
	private Object name;
	private Object value;

	public Object getValue() {
		return value;
	}

	public XMLAttr(Object name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		buf.addContent(name);
		buf.addContent("=");
		buf.addContent(value);
		return buf;
	}
}