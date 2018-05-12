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
	
	/**
	 * ajout les media dans la sousclasse CSSMedia
	 */
	protected void before()
	{
	}
	
	/**
	 * ajout les media dans la sousclasse CSSMedia
	 */
	protected void after()
	{
		
	}
	
	@Deprecated
	public CSSElement on(Object path, Object content)
	{
		getListStyle().add(new CSSStyleRow(path, content));
		return this;
	}
	
	@Deprecated
	public CSSElement path(Object... path)
	{
		getListStyle().add(new CSSStyleRow(CSSSelector.onPath(path), null));
		currentCSSStyle = getListStyle().getLast();
		return this;
	}
	
	@Deprecated
	public CSSElement andChild(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, " ", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	@Deprecated
	public CSSElement andDirectChild(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ">", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	@Deprecated
	public CSSElement or(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, ",", cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	@Deprecated
	public CSSElement and(CSSElement content)
	{
		LinkedList<CSSStyleRow> path = content.getListStyle();
		
		for (CSSStyleRow cssStyle : path) {
			getListStyle().add(new CSSStyleRow(CSSSelector.onPath(currentCSSStyle.path, cssStyle.path), cssStyle.content));
		}
		
		return this;
	}
	
	@Deprecated
	public CSSElement set(String content)
	{
		content=content.trim();		
		getListStyle().getLast().content.add(content);
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
