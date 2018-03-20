/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css.selector;

import com.elisaxui.core.xui.xhtml.builder.css.CSSElement;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyleRow;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
public class CSSMedia extends CSSElement {

	Object mediaQuery;

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		CSSStyleRow media = this.getListStyle().removeFirst();
		mediaQuery = media.getPath();
		
		for (CSSStyleRow elem : this.getListStyle()) {
			elem.setNbTabInternal(1);
		}
		
		return super.toXML(buf);
	}

	@Override
	protected void before() {
		listInner.add(new CSSStyleRowMedia( mediaQuery + " {", null));
	}

	@Override
	protected void after() {
		listInner.add(new CSSStyleRowMedia("}", null));
	}

	
	class CSSStyleRowMedia extends CSSStyleRow
	{

		public CSSStyleRowMedia(Object path, Object content) {
			super(path, content);
		}
		
		@Override
		public XMLBuilder toXML(XMLBuilder buf)
		{
			String style = getPath().toString();
			if (style!=null)
			{
				newLine(buf);
				newTabInternal(buf);
				buf.addContentOnTarget(style);
			}
			return buf;
		}
	}
}
