package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.LinkedList;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public class CSSElement  extends XMLElement {
	
	public CSSElement()
	{
		super(XHTMLPart.STYLE, null);
	}
	
	public CSSElement(Object... inner)
	{
		super(XHTMLPart.STYLE, inner);
	}
	
	private LinkedList<CSSStyleRow> listStyle = new LinkedList<>();
	private CSSStyleRow currentCSSStyle =null;

	/**
	 * @return the currentCSSStyle
	 */
	public final CSSStyleRow getCurrentCSSStyle() {
		return currentCSSStyle;
	}

	/**
	 * @param currentCSSStyle the currentCSSStyle to set
	 */
	public final void setCurrentCSSStyle(CSSStyleRow currentCSSStyle) {
		this.currentCSSStyle = currentCSSStyle;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		
		before();
		
		for (CSSStyleRow object : getListStyle()) {
				listInner.add(((CSSStyleRow) object));
		}
		
		after();
		
		return super.toXML(buf);
	}
	
	protected void before()
	{
		
	}
	
	protected void after()
	{
		
	}
	
	@Deprecated
	public CSSElement on(Object path, Object content)
	{
		getListStyle().add(new CSSStyleRow(path, content));
		return this;
	}
	
	public CSSElement path(Object... path)
	{
		getListStyle().add(new CSSStyleRow(CSSSelector.onPath(path), null));
		currentCSSStyle = getListStyle().getLast();
		return this;
	}
	
	
	public CSSElement andChild(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, " ", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSElement andDirectChild(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ">", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSElement or(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ",", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSElement and(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	public CSSElement set(String content)
	{
		String cssContent = (String) getListStyle().getLast().content;
		content=content.trim();
		if (cssContent==null)
			getListStyle().getLast().content = content;
		else
		{
			boolean endWithSep = cssContent.length()==0?false:(cssContent.charAt(cssContent.length()-1)==';');
			boolean startWithSep = cssContent.length()==0?true:(content.charAt(0)==';');
			
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
