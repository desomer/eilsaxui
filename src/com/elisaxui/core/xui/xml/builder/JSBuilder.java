package com.elisaxui.core.xui.xml.builder;

import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class JSBuilder extends Element {
	
	public JSBuilder(Object name, Object[] inner) {
		super(name, inner);
	}


	public JSContent createJSContent()
	{
		return new JSContent();
	}
	
	
	public  final class JSContent implements XMLBuilder.IXMLBuilder
	{
		LinkedList<Object> listElem = new LinkedList<Object>();

		public JSBuilder getJSBuilder()
		{
			return JSBuilder.this;
		}
		
		@Override
		public XMLBuilder toXML(XMLBuilder buf) {
			for (Object object : listElem) {
				if (object == JSNewLine.class)
				{
					JSBuilder.this.newLine(buf);
					JSBuilder.this.newTabulation(buf);
				}
				else if (object instanceof Element)
				{
					StringBuilder txtXML = new StringBuilder(1000);
					StringBuilder txtXMLAfter = new StringBuilder(1000);
					((Element)object).toXML(new XMLBuilder("js", txtXML, txtXMLAfter));
					buf.addContent("[(function () {/*");
					buf.addContent(txtXML);
					buf.addContent("\n*/}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]");
					buf.addContent(", (function () {/*");
					buf.addContent(txtXMLAfter.toString().replace("</script>", "<\\/script>"));
					buf.addContent("\n*/}).toString().replace('<\\\\/script>','</'+'script>').match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]");
					buf.addContent("]");
					
//					var myString = (function () {/*
//						   <div id="someId">
//						     some content<br />
//						     <a href="#someRef">someRefTxt</a>
//						    </div>        
//						*/}).toString().match(/[^]*\/\*([^]*)\*\/\}$/)[1];
				}
				else
					buf.addContent(object);
			}
			return buf;
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
		
		public JSContent var(Object name, Object ...content)
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
	
	public static final class JSNewLine {};
	
	
	
	public static final class JSElem
	{
		public Object value; 
	}
}
