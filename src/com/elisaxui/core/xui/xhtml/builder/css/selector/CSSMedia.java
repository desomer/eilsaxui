/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css.selector;

import com.elisaxui.core.xui.xhtml.builder.css.CSSElement;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyleRow;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class CSSMedia extends CSSElement {

	Object mediaQuery;  // a null si all

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		CSSStyleRow media = this.getListStyle().removeFirst();
		mediaQuery = media.getPath();
		if (mediaQuery!=null)
			for (CSSStyleRow elem : this.getListStyle()) {
				elem.setNbTabInternal(1);  // ajoute une tabulation sur les sous lignes du css
			}
		
		return super.toXML(buf);
	}

	@Override
	protected void before() {
		if (mediaQuery!=null)
			listInner.add(new CSSStyleRowMedia( mediaQuery + " {", null));
	}

	@Override
	protected void after() {
		if (mediaQuery!=null)
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
			if (getPath()!=null)
			{
				String style = getPath().toString();
				newLine(buf);
				newTabInternal(buf);
				buf.addContentOnTarget(style);
			}
			return buf;
		}
	}
}
