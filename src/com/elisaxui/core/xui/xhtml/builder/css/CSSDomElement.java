package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public class CSSDomElement  extends XMLElement {
	
	public CSSDomElement()
	{
		super("style", null);
	}
	
	public CSSDomElement(Object... inner)
	{
		super("style", inner);
	}
	


	private LinkedList<CSSStyleRow> listStyle = new LinkedList<>();
	private CSSStyleRow currentCSSStyle =null;

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		for (Object object : getListStyle()) {
			if (object instanceof XMLAttr) {
				listAttr.add((XMLAttr) object);   //????????? pourquoi XMLAttr
			} else if (object instanceof CSSStyleRow) {
				listInner.add(((CSSStyleRow) object));
			} else {
				listInner.add(object.toString());
			}
		}
		
		return super.toXML(buf);
	}
	
	@Deprecated
	public CSSDomElement on(Object path, Object content)
	{
		getListStyle().add(new CSSStyleRow(path, content));
		return this;
	}
	
	public CSSDomElement path(Object... path)
	{
		getListStyle().add(new CSSStyleRow(CSSSelector.onPath(path), null));
		currentCSSStyle = getListStyle().getLast();
		return this;
	}
	
	
	public CSSDomElement andChild(CSSDomElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, " ", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSDomElement andDirectChild(CSSDomElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ">", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSDomElement or(CSSDomElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ",", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSDomElement and(CSSDomElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSDomElement set(String content)
	{
		String cssContent = (String) getListStyle().getLast().content;
		content=content.trim();
		if (cssContent==null)
			getListStyle().getLast().content = content;
		else
		{
			boolean endWithSep = cssContent.length()==0?false:(cssContent.charAt(cssContent.length()-1)==';');
			boolean startWithSep = content.charAt(0)==';';
			
			getListStyle().getLast().content = cssContent+( (!endWithSep && !startWithSep)?";":"")+content;
		}
		return this;	
	}

	/**
	 * @return the listStyle
	 */
	public LinkedList<CSSStyleRow> getListStyle() {
		return listStyle;
	}

	/**
	 * @param listStyle the listStyle to set
	 */
	public void setListStyle(LinkedList<CSSStyleRow> listStyle) {
		this.listStyle = listStyle;
	}
}
