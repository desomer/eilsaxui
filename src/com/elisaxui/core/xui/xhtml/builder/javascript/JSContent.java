package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInvocationHandler;
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
	protected JSContent(JSBuilder jsBuilder) {
		this.jsBuilder = jsBuilder;
	}

	private LinkedList<Object> listElem = new LinkedList<Object>();

	public JSBuilder getJSBuilder() {
		return this.jsBuilder;
	}

	protected void newLine(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\n");
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		for (Object object : getListElem()) {
			if (object == JSNewLine.class) {
				this.jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
			} else if (object == JSAddTab.class) {
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() + 1);
			} else if (object == JSRemoveTab.class) {
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() - 1);
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
			} 
			else if (object instanceof JSContent) {	
				((JSContent)object).toXML(buf);
			} 
			else {
				if (buf.isJS())
					// ajout d'un JS sous forme de text
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
		
		String txtJS = txtXMLAfter.toString().replace("</script>", "<\\/script>");
		
		// gestion d'ajout d'un JSXHTMLPart dans un autre JSXHTMLPart
		if (txtJS.contains("new JSXHTMLPart("))	{
			txtJS=	txtJS.replace("new JSXHTMLPart('", "new JSXHTMLPart(\\'");
			txtJS = txtJS.replace("');", ");");
		}	
		
		buf.addContent("new JSXHTMLPart('");
		buf.addContent(txtXML);
		buf.addContent("',");
		newLine(buf);
		buf.addContent("'");
		buf.addContent(txtJS);
		buf.addContent("')");
	}

	/**
	 * utiliser par les set et var avec un new
	 * @param name
	 * @param object
	 */
	private void addElem(Object name, Object object) {
		if (object instanceof JSListParameter && name instanceof JSClass) {
			JSClassInvocationHandler inv = (JSClassInvocationHandler) Proxy.getInvocationHandler(name);
			getListElem().add(JSClass._new(inv.getImplementClass(), ((JSListParameter) object).param));
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
				getListElem().add(object2);
			}
		} else
			getListElem().add(object);
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
		getListElem().add(JSNewLine.class);
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(";");
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
		getListElem().add(JSNewLine.class);
		getListElem().add(name);
		getListElem().add("=");
		if (content != null) {
			for (Object object : content) {
				addElem(name, object);
			}
		} else
			getListElem().add("null");
		getListElem().add(";");
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
		getListElem().add(JSNewLine.class);
		getListElem().add("var ");
		getListElem().add(name);
		getListElem().add("=");
		for (Object object : content) {
			addElem(name, object);  // pour le new class
		}
		getListElem().add(";");
		return this;
	}

	@Override
	public JSMethodInterface consoleDebug(Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("console.debug(");
		int i = 0;
		for (Object object : content) {
			addElem(object);
			i++;
			if (i < content.length)
				getListElem().add(",");
		}
		getListElem().add(");");
		return this;
	}

	@Override
	public JSMethodInterface _for(Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("for (");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(") {");
		return this;
	}

	@Override
	public JSMethodInterface endfor() {
		getListElem().add(JSNewLine.class);
		getListElem().add("}");
		return this;
	}

	/*****************************************************/
	@Override
	public JSMethodInterface _if(Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("if (");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(") {");
		getListElem().add(JSAddTab.class);
		return this;
	}

	@Override
	public JSMethodInterface _else() {
		getListElem().add(JSRemoveTab.class);
		getListElem().add(JSNewLine.class);
		getListElem().add("} else {");
		getListElem().add(JSAddTab.class);
		return this;
	}
	
	@Override
	public JSMethodInterface _elseif(Object... content) {
		getListElem().add(JSRemoveTab.class);
		getListElem().add(JSNewLine.class);
		getListElem().add("} else if(");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(") {");
		getListElem().add(JSAddTab.class);
		return this;
	}

	@Override
	public JSMethodInterface endif() {
		getListElem().add(JSRemoveTab.class);
		getListElem().add(JSNewLine.class);
		getListElem().add("}");
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
		getListElem().add(JSNewLine.class);
		getListElem().add("return ");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(";");
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

	/**
	 * @return the listElem
	 */
	public LinkedList<Object> getListElem() {
		return listElem;
	}

	/**
	 * @param listElem the listElem to set
	 */
	public void setListElem(LinkedList<Object> listElem) {
		this.listElem = listElem;
	}

	

	// new line 
	public static final class JSNewLine {
	};
	public static final class JSAddTab {
	};
	public static final class JSRemoveTab {
	};
}