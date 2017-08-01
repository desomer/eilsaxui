package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public class CSSBuilder  extends XMLElement {
	
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
	}


	private LinkedList<CSSStyle> listStyle = new LinkedList<CSSStyle>();

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		for (Object object : listStyle) {
			if (object instanceof XMLAttr) {
				listAttr.add((XMLAttr) object);
			} else if (object instanceof CSSStyle) {
				listInner.add(((CSSStyle) object));
			} else {
				listInner.add(object.toString());
			}
		}
		
		return super.toXML(buf);
	}
	
	@Deprecated
	public CSSBuilder on(Object path, Object content)
	{
		listStyle.add(new CSSStyle(path, content));
		return this;
	}
	
	public CSSBuilder select(Object... path)
	{
		listStyle.add(new CSSStyle(CSSSelector.onPath(path), null));
		return this;
	}
	
	
	public CSSBuilder path(CSSBuilder content)
	{
		LinkedList<CSSStyle> path = content.listStyle;
		
		for (CSSStyle cssStyle : path) {
			listStyle.add(new CSSStyle(CSSSelector.onPath(listStyle.getLast().path, " ", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSBuilder set(Object content)
	{
		Object cssContent = listStyle.getLast().content;
	//	if (cssContent==null)
			listStyle.getLast().content = content;
	//	else
//		{
//			
//		}
			
		return this;	
	}
}
