package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class CSSBuilder  extends Element {
	
	public CSSBuilder()
	{
		super("style", null);
	}
	
	public CSSBuilder(Object... inner)
	{
		super("style", inner);
	}
	
	private CSSBuilder(Object name, Object... inner) {
		super("style", inner);
		// TODO Auto-generated constructor stub
	}


	private LinkedList<CSSStyle> listStyle = new LinkedList<CSSStyle>();

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		for (Object object : listStyle) {
			if (object instanceof Attr) {
				listAttr.add((Attr) object);
			} else {
				listInner.add(object.toString());
			}
		}
		
		return super.toXML(buf);
	}
	
	public CSSBuilder on(Object path, Object content)
	{
		listStyle.add(new CSSStyle(path, content));
		return this;
	}
	
	
	public static final class CSSStyle {
		Object path;
		Object content;

		@Override
		public String toString() {
			if (path instanceof CSSClass)
				return  "."+((CSSClass)path).getId() + " { " +content +" }\n";
			else
				return  path + " {" +content +"}\n";
		}

		public CSSStyle(Object path, Object content) {
			super();
			this.path = path;
			this.content = content;
		}
		
	}
}
