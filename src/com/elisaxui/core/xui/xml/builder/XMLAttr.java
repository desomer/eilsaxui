/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLHandle;

/**
 * un attribut XML
 * 
 * @author Bureau
 *
 */
public class XMLAttr implements IXMLBuilder {
	private Object name;
	/**
	 * @return the name
	 */
	public final Object getName() {
		return name;
	}

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
		buf.addContentOnTarget(name);
		if (value!=null)
		{
			buf.addContentOnTarget("=");
			if (value instanceof VProperty)
			{
				Object v = XMLElement.zzGetProperties(new XMLHandle(((VProperty)value).getName()));
				buf.addContentOnTarget(v);
			}
			else
				buf.addContentOnTarget(value);
		}
		return buf;
	}
}