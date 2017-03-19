package com.elisaxui.core.xui.xml;

import java.util.ArrayList;
import java.util.List;

import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Part;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;

public class XMLPart {

	public void doContent(XMLPart root) {}
	public void doRessource(XMLPart root) {}
	
	private final XMLBuilder xmlBuilder = new XMLBuilder("main", null, null);
	private Element content;
	public Element getContent() {
		return content;
	}
	
	private Element after;
	public Element getAfter() {
		return after;
	}

	private final List<Object> children = new ArrayList<>();
	
	public List<Object> getChildren() {
		return children;
	}
	public final void xContent(Element part)
	{
		content=part;
	}
	
	public final void xAfter(Element part)
	{
		after=part;
	}
	
	
	public final Part xPart(XMLPart part, Object...inner )
	{
		return xmlBuilder.getPart(part, inner);
	}
	
	public final Element xElement(String name, Object...inner )
	{
		Element tag = xmlBuilder.getElement(name, inner);
		return tag;
	}
	
	
	public final Attr xAttr(String name, Object value )
	{
		Attr attr = xmlBuilder.getAttr(name, value);
		return attr;
	}
	
	
}
