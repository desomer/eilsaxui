/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.LinkedList;

import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLHandle;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * la ligne de style
 * 
 * @author gauth
 *
 */
public class CSSStyleRow extends XUIFormatManager implements IXMLBuilder {
	Object path;

	public final Object getPath() {
		return path;
	}

	LinkedList<Object> content = new LinkedList<>();

	public CSSStyleRow(Object path, Object cont) {
		super();
		this.path = path;

		if (cont instanceof LinkedList)
			this.content = (LinkedList<Object>) cont;
		else if (cont != null) {
			this.content.addLast(cont);
		}
	}

	public XMLBuilder toXML(XMLBuilder buf) {
		if (content.size() == 0)
			return null;

		newLine(buf);
		newTabInternal(buf);

		if (path instanceof CSSClass)
			buf.addContentOnTarget("." + ((CSSClass) path).getId() + " { ");
		else
			buf.addContentOnTarget(path + " {");

		int i = 0;
		String last = null;

		for (Object object : content) {
			boolean endWithSep = last==null ? false : (last.length()>0 && last.charAt(last.length() - 1) == ';');

			if (object instanceof XMLHandle) {
				Object handledObject = XMLElement.zzGetProperties((XMLHandle) object);
				if (handledObject != null) {
					last = handledObject.toString().trim();
					if (last.length() > 0) {
						boolean startWithSep = i == 0 ? true : (last.charAt(0) == ';');
						buf.addContentOnTarget(((!endWithSep && !startWithSep) ? ";" : "") + last);
					}
				}
			} else if (object instanceof VProperty) {
				VProperty h = (VProperty) object;
				XMLHandle handle = new XMLHandle(h.getName());

				Object handledObject = XMLElement.zzGetProperties(handle);
				if (handledObject != null) {
					last = handledObject.toString().trim();
					if (last.length() > 0) {
						boolean startWithSep = i == 0 ? true : (last.charAt(0) == ';');
						buf.addContentOnTarget(((!endWithSep && !startWithSep) ? ";" : "") + last);
					}
				}
			} else {
				last = object.toString();
				if (last.length() > 0) {
					boolean startWithSep = i == 0 ? true : (last.charAt(0) == ';');
					buf.addContentOnTarget(((!endWithSep && !startWithSep) ? ";" : "") + last);
				}
			}

			i++;
		}

		buf.addContentOnTarget("}");

		return buf;
	}

}