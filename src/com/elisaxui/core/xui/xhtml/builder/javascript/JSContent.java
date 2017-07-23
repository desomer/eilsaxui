package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder.JSNewLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder.aInvocationHandler;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * super class de JSClass et JSFunction
 * herite de JSMethod
 * 
 * @author Bureau
 *
 */
public class JSContent implements IXMLBuilder, JSMethodInterface {
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
		if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\n");
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		for (Object object : listElem) {
			if (object == JSNewLine.class) {
				this.jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
			} else if (object instanceof XMLElement) {
				doXMLElement(buf, ((XMLElement) object));
			} else if (object instanceof JSFunction) {
				JSFunction fct = (JSFunction) object;
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() + 1);
				fct.toXML(buf);
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() - 1);
				jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
//			} else if (object instanceof JSContent && (this != object)) {
//				JSContent c = (JSContent) object;
//				c.toXML(buf);
			} else if (object instanceof JSVariable) {	
				buf.addContent(((JSVariable)object).toString());
			} else {
				if (buf.isJS())
					// ajout d'un JS
					buf.addContent(object.toString().replaceAll("'", "\\\\'"));
				else
					buf.addContent(object);
			}
		}
		return buf;
	}

	/**
	 * ajout d'une div+js (XMLElement dans un JS)
	 * @param buf
	 * @param elem
	 */
	private void doXMLElement(XMLBuilder buf, XMLElement elem) {
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

	/**
	 * utiliser par les set et var avec un new
	 * @param name
	 * @param object
	 */
	private void addElem(Object name, Object object) {
		if (object instanceof JSListParameter && name instanceof JSClass) {
			aInvocationHandler inv = (aInvocationHandler) Proxy.getInvocationHandler(name);
			listElem.add(JSClass._new(inv.implementClass, ((JSListParameter) object).param));
		} else
			addElem(object);
	}

	/**
	 * Boucle sur un tableau
	 * @param object
	 */
	private void addElem(Object object) {
		if (object instanceof List && ! (object instanceof JSMethodInterface)) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) object;
			for (Object object2 : list) {
				listElem.add(object2);
			}
		} else
			listElem.add(object);
	}

	/**************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.elisaxui.core.xui.xml.builder.javascript.JSInterface#__(java.lang.
	 * Object)
	 */
	@Override
	public JSMethodInterface __(Object... content) {
		listElem.add(JSNewLine.class);
		for (Object object : content) {
			addElem(object);
		}
		listElem.add(";");
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.elisaxui.core.xui.xml.builder.javascript.JSInterface#set(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
	public JSMethodInterface set(Object name, Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add(name);
		listElem.add("=");
		if (content != null) {
			for (Object object : content) {
				addElem(name, object);
			}
		} else
			listElem.add("null");
		listElem.add(";");
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.elisaxui.core.xui.xml.builder.javascript.JSInterface#var(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
	public JSMethodInterface var(Object name, Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("var ");
		listElem.add(name);
		listElem.add("=");
		for (Object object : content) {
			addElem(name, object);  // pour le new class
		}
		listElem.add(";");
		return this;
	}

	@Override
	public JSMethodInterface consoleDebug(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("console.debug(");
		int i = 0;
		for (Object object : content) {
			addElem(object);
			i++;
			if (i < content.length)
				listElem.add(",");
		}
		listElem.add(");");
		return this;
	}

	@Override
	public JSMethodInterface _for(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("for (");
		for (Object object : content) {
			addElem(object);
		}
		listElem.add(") {");
		return this;
	}

	@Override
	public JSMethodInterface endfor() {
		listElem.add(JSNewLine.class);
		listElem.add("}");
		return this;
	}

	@Override
	public JSMethodInterface _if(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("if (");
		for (Object object : content) {
			addElem(object);
		}
		listElem.add(") {");
		return this;
	}

	@Override
	public JSMethodInterface _else() {
		listElem.add(JSNewLine.class);
		listElem.add("} else {");
		return this;
	}
	
	@Override
	public JSMethodInterface _elseif(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("} else if(");
		for (Object object : content) {
			addElem(object);
		}
		listElem.add(") {");
		return this;
	}

	@Override
	public JSMethodInterface endif() {
		listElem.add(JSNewLine.class);
		listElem.add("}");
		return this;
	}

	/********************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.elisaxui.core.xui.xml.builder.javascript.JSInterface#_new(java.lang.
	 * Object)
	 */
	@Override
	public Object _new(Object... param) {
		return new JSListParameter(param);
	}

	@Override
	public Object txt(Object... param) {
		return "\"" + param[0] + "\"";
	}

	@Override
	public JSFunction fct(Object... param) {
		return jsBuilder.createJSFunction().setParam(param);
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_void()
	 */
	@Override
	public JSMethodInterface _void() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_null()
	 */
	@Override
	public JSMethodInterface _null() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#_this()
	 */
	@Override
	public Object _this() {
		return "this";
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#_return(java.lang.Object[])
	 */
	@Override
	public JSMethodInterface _return(Object... content) {
		listElem.add(JSNewLine.class);
		listElem.add("return ");
		for (Object object : content) {
			addElem(object);
		}
		listElem.add(";");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#jsvar(java.lang.Object[])
	 */
	@Override
	public Object jsvar(Object... param) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			str.append(param[i]);
		}
		return str.toString();
	}

	

}