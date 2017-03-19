package com.elisaxui.core.xui.xml;

import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Tag;

public class XMLPart {

	public void doContent(XMLFile file) {}
	public void doRessource(XMLFile file) {}
	
	XMLBuilder xmlBuilder = new XMLBuilder("main", null);
	
	
	public Tag xTag(String name, Object...inner )
	{
		Tag tag = xmlBuilder.getTag(name, inner);
		return tag;
	}
	
	
	public Attr xAttr(String name, Object value )
	{
		Attr attr = xmlBuilder.getAttr(name, value);
		return attr;
	}
	
}
