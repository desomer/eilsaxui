package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.Proxy;
import java.util.LinkedList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder.JSNewLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder.aInvocationHandler;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class JSContent implements XMLBuilder.IXMLBuilder, JSInterface {
	/**
	 * 
	 */
	protected final JSBuilder jsBuilder;

	/**
	 * @param jsBuilder
	 */
	JSContent(JSBuilder jsBuilder) {
		this.jsBuilder = jsBuilder;
	}

	LinkedList<Object> listElem = new LinkedList<Object>();

	public JSBuilder getJSBuilder() {
		return this.jsBuilder;
	}

	protected void newLine(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\n");
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		for (Object object : listElem) {
			if (object == JSNewLine.class) {
				this.jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
			} else if (object instanceof Element) {
				doXMLElement(buf, ((Element) object));
			} else {
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

		buf.addContent("new JSXHTMLPart('");
		buf.addContent(txtXML);
		buf.addContent("',");
		newLine(buf);
		buf.addContent("'");
		buf.addContent(txtXMLAfter.toString().replace("</script>", "<\\/script>"));
		buf.addContent("')");
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xml.builder.javascript.JSInterface#__(java.lang.Object)
	 */
	@Override
	public JSInterface __(Object... content) {
		listElem.add(JSNewLine.class);
		for (Object object : content) {
			listElem.add(object);
		}
		listElem.add(";");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xml.builder.javascript.JSInterface#set(java.lang.Object, java.lang.Object)
	 */
	@Override
	public JSInterface set(Object name, Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add(name);
		listElem.add("=");
		if (content != null) {
			for (Object object : content) {
				addElem(name, object);
			}
		}
		else
			listElem.add("null");
		listElem.add(";");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xml.builder.javascript.JSInterface#var(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSInterface var(Object name, Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("var ");
		listElem.add(name);
		listElem.add("=");
		for (Object object : content) {
			addElem(name, object);
		}
		listElem.add(";");
		return this;
	}

	private void addElem(Object name, Object object) {
		if (object instanceof JSVariable && name instanceof JSClass)
		{
			aInvocationHandler inv = (aInvocationHandler) Proxy.getInvocationHandler(name);
			listElem.add(JSClass._new( inv.interfac, ((JSVariable)object).param));
		}
		else
		 listElem.add(object);
	}
	
	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xml.builder.javascript.JSInterface#_new(java.lang.Object)
	 */
	@Override
	public Object _new(Object... param) {
		return new JSVariable(param);
	}

	@Override
	public JSInterface consoleDebug(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("console.debug(");
		for (Object object : content) {
			listElem.add(object);
		}
		listElem.add(");");
		return this;
	}

	@Override
	public JSInterface _for(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("for (");
		for (Object object : content) {
			listElem.add(object);
		}
		listElem.add(") {");
		return this;
	}

	@Override
	public JSInterface endfor(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("}");
		return this;
	}

	@Override
	public Object txt(Object... param) {
			return "\""+ param[0] + "\"";
	}
}