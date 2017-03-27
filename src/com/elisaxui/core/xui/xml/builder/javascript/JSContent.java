package com.elisaxui.core.xui.xml.builder.javascript;

import java.util.LinkedList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder.JSNewLine;

public class JSContent implements XMLBuilder.IXMLBuilder
{
	/**
	 * 
	 */
	private final JSBuilder jsBuilder;

	/**
	 * @param jsBuilder
	 */
	JSContent(JSBuilder jsBuilder) {
		this.jsBuilder = jsBuilder;
	}

	LinkedList<Object> listElem = new LinkedList<Object>();

	public JSBuilder getJSBuilder()
	{
		return this.jsBuilder;
	}
	
	protected void newLine(XMLBuilder buf) {
			if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrJS())
				buf.addContent("\n");		
	}
		
	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		for (Object object : listElem) {
			if (object == JSNewLine.class)
			{
				this.jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
			}
			else if (object instanceof Element)
			{
				doXMLElement(buf, ((Element)object));
			}
			else
			{
				if (buf.isJS())
					buf.addContent(object.toString().replaceAll("'", "\\\\'"));
				else
					buf.addContent(object);
			}
		}
		return buf;
	}

	private void doXMLElement(XMLBuilder buf, Element elem) {
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);
		elem.toXML(new XMLBuilder("js", txtXML, txtXMLAfter).setJS(true));
		
		buf.addContent("['");
		buf.addContent(txtXML);
		buf.addContent("',");
		newLine(buf);
		buf.addContent("'");
		buf.addContent(txtXMLAfter.toString().replace("</script>", "<\\/script>"));
		buf.addContent("']");
	}
	
	public JSContent __(Object...content)
	{
		listElem.add(JSNewLine.class);
		for (Object object : content) {
			listElem.add(object);
		}
		listElem.add(";");
		return this;
	}
	
	public JSContent _var(Object name, Object ...content)
	{
		listElem.add(JSNewLine.class);
		listElem.add("var ");
		listElem.add(name);
		listElem.add("=");
		for (Object object : content) {
			listElem.add(object);
		}
		listElem.add(";");
		return this;
	}
	
}