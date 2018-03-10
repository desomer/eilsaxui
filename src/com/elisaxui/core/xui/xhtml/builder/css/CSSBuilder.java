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
	


	private LinkedList<CSSStyle> listStyle = new LinkedList<>();
	private CSSStyle currentCSSStyle =null;

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
	
	public CSSBuilder path(Object... path)
	{
		listStyle.add(new CSSStyle(CSSSelector.onPath(path), null));
		currentCSSStyle = listStyle.getLast();
		return this;
	}
	
	
	public CSSBuilder andChildPath(CSSBuilder content)
	{
		LinkedList<CSSStyle> path = content.listStyle;
		
		for (CSSStyle cssStyle : path) {
			listStyle.add(new CSSStyle(CSSSelector.onPath(currentCSSStyle.path, " ", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSBuilder andDirectChildPath(CSSBuilder content)
	{
		LinkedList<CSSStyle> path = content.listStyle;
		
		for (CSSStyle cssStyle : path) {
			listStyle.add(new CSSStyle(CSSSelector.onPath(currentCSSStyle.path, ">", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSBuilder orPath(CSSBuilder content)
	{
		LinkedList<CSSStyle> path = content.listStyle;
		
		for (CSSStyle cssStyle : path) {
			listStyle.add(new CSSStyle(CSSSelector.onPath(currentCSSStyle.path, ",", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSBuilder andPath(CSSBuilder content)
	{
		LinkedList<CSSStyle> path = content.listStyle;
		
		for (CSSStyle cssStyle : path) {
			listStyle.add(new CSSStyle(CSSSelector.onPath(currentCSSStyle.path, cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSBuilder add(String content)
	{
		String cssContent = (String) listStyle.getLast().content;
		content=content.trim();
		if (cssContent==null)
			listStyle.getLast().content = content;
		else
		{
			boolean endWithSep = cssContent.charAt(cssContent.length()-1)==';';
			boolean startWithSep = content.charAt(0)==';';
			
			listStyle.getLast().content = cssContent+( (!endWithSep && !startWithSep)?";":"")+content;
		}
		return this;	
	}
}
