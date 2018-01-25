package com.elisaxui.core.xui.xhtml.builder.javascript;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
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
	public JSMethodInterface proxy;
	
	/**************************************************************/
	// new line 
	public static final class JSNewLine {
	};
	public static final class JSAddTab {
	};
	public static final class JSRemoveTab {
	}
	/**************************************************************/
	
	/**
	 * @param jsBuilder
	 */
	protected JSContent(JSBuilder jsBuilder) {
		this.jsBuilder = jsBuilder;
	}

	private LinkedList<Object> listElem = new LinkedList<Object>();

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

	
	public JSBuilder getJSBuilder() {
		return this.jsBuilder;
	}

	protected final void newLine(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\n");
	}
	
	protected final void newTab(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\t");
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		for (Object object : getListElem()) {
			addXML(buf, object);
		}
		return buf;
	}

	/**
	 * @param buf
	 * @param object
	 */
	private void addXML(XMLBuilder buf, Object object) {
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
			if (fct.isFragment())
			{
				fct.toXML(buf);
			}
			else
			{
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() + 1);
				fct.toXML(buf);
				jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab() - 1);
				jsBuilder.newLine(buf);
				this.jsBuilder.newTabulation(buf);
			}
		} 
		else if (object instanceof JSVariable) {	
			Object v = ((JSVariable)object)._getString();
			if (v instanceof Array)
			{
				Array arr = (Array)v;
				for (Object object2 : arr) {
					addXML(buf, object2);
				}
			}
			else
				buf.addContent(((JSVariable)object).toString());
		} 
		else if (object instanceof JSClass) {	
			Object v = ((JSClass)object)._getContent();  // recup de la valeur du proxy
			if (v instanceof Array)
			{
				Array arr = (Array)v;
				for (Object object2 : arr) {
					addXML(buf, object2);
				}
			}
			else if (v!=null)
				buf.addContent(v);
			else
				buf.addContent(object.toString());  // recup du nom du proxy
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

	/**
	 * ajout d'une div+js (XMLElement dans un JS)
	 * @param buf
	 * @param elem
	 */
	private void doXMLElement(XMLBuilder buf, XMLElement elem) {
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);
		
		XMLBuilder elemJS =  new XMLBuilder("js", txtXML, txtXMLAfter);
		elem.setNbInitialTab(jsBuilder.getNbInitialTab()+2);
		
		elem.toXML(elemJS.setJS(true));  // charge les string
		
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
			for (int i = 0; i < jsBuilder.getNbInitialTab()+2; i++) {
				newTab(buf);
			}
	    if ( XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS() )
	    	buf.addContent("/*JS Template*/");
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
			// gestion du new
			MethodInvocationHandler inv = (MethodInvocationHandler) Proxy.getInvocationHandler(name);
			getListElem().add(JSClass._new(inv.getImplementClass(), ((JSListParameter) object).param));
		} else
			addElem(object);
	}

	/**
	 * Boucle sur un tableau
	 * @param object
	 */
	private void addElem(Object object) {
		if (object instanceof List /*&& ! (object instanceof JSMethodInterface)*/) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) object;
			for (Object object2 : list) {
				addElem(object2);
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
		if (content!=null)
		{
			if (content.length==1 && content[0] instanceof JSFunction)
			{
				JSFunction fct = ((JSFunction)  content[0] );
//				if (!fct.isActived()) 
//						return this;
				
				fct.setFragment(true);
			}
			else
				getListElem().add(JSNewLine.class);
			
			for (Object object : content) {
				addElem(object);
			}
			getListElem().add(";");
		}
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
	public JSMethodInterface _set(Object name, Object... content) {
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
	public JSMethodInterface _var(Object name, Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("let ");
		getListElem().add(name);
		getListElem().add("=");
		if (content==null)
		{
			addElem(null);
		}
		else {
			for (Object object : content) {
				addElem(name, object);  // pour le new class
			}
		}
		getListElem().add(";");
		return this;
	}

	
	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#setTimeout(java.lang.Object[])
	 */
	@Override
	public JSMethodInterface setTimeout(Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("setTimeout(");
			int i=0;
			for (Object object : content) {
				if (i>0)
					addElem(",");
				addElem(object);
				i++;
			}

		getListElem().add(");");
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
	public JSMethodInterface systemDebugIf(Object cond, Object... content) {
		Array<Object> p = new Array<Object>();
		p.add("'<SYSTEM>'");
		p.addAll(Arrays.asList(content));
		
		return __(fragmentIf(cond).consoleDebug(p.toArray()));
	};

	
	/***************************************************************************/
	
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
	public JSMethodInterface _forIdx(Object idx, JSArray array) {
		return _for("var "+idx+" = 0, "+idx+"len =", array.length(), "; "+idx+" < "+idx+"len; "+idx+"++");
	}
	
	@Override
	public JSMethodInterface _forIdxBetween(JSInt idx, int start, int end) {
		return _for("var "+idx+" = "+start+";"+idx+" < "+end+"; "+idx+"++");
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
	public JSMethodInterface _elseif_(Object... content) {
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
	public JSMethodInterface _elseif(Object... content) {
		getListElem().add(" else if(");
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


	@Override
	public Object _new(Object... param) {
		
		if (param.length>0 && param[0] instanceof Class)
			return MethodInvocationHandler.cast((Class)param[0], JSClass._new((Class)param[0], Arrays.copyOfRange(param, 1, param.length)));
		else
			return new JSListParameter(param);
	}
	
	@Override
	public <E> E newInst(Class type, Object... param) {
			return (E)MethodInvocationHandler.cast(type, JSClass._new(type, param));
	}

	@Override
	public Object txt(Object... param) {
		
		StringBuilder str = new StringBuilder();
		str.append("\"");
		for (int i = 0; i < param.length; i++) {
			if (param[i] instanceof JSVariable)
			{
				str.append("\"+");
				str.append(param[i]);
				str.append("+\"");
			}
			else
				str.append(param[i]);
		}
		str.append("\"");
		return new JSString()._setContent(str.toString());
	}

	/***************************************************************/
	
	@Override
	public JSFunction fct(Object... param) {
		return jsBuilder.createJSFunction().setParam(param);
	}
	
	@Override
	public JSFunction callback(Anonym call) {
		JSFunction ret =  jsBuilder.createJSFunction();
		ret.proxy = (JSMethodInterface) MethodInvocationHandler.ThreadLocalMethodDesc.get().proxy;
		ret.__(call);
		return ret;
	}
	
	@Override
	public JSFunction callback(Object param, Anonym call) {
		JSFunction ret =  jsBuilder.createJSFunction();
		ret.proxy = (JSMethodInterface) MethodInvocationHandler.ThreadLocalMethodDesc.get().proxy;
		ret.__(call);
		return ret;
	}
	
	@Override
	public JSFunction fragmentIf(Object cond) {
		
		return jsBuilder.createJSFunction().setFragment(true).setActivatedCondition(cond);
	}

	/************************************************************************/
	
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

	
	@Override
	public JSVariable var(Object... param) {
		return XHTMLPart.jsvar(param);
	}

	public JSVariable calc(Object... param)
	{
		return var(param);
	}

	/**************************** FORMATAGE  ********************************/

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_void()
	 */
	@Override
	@Deprecated
	public JSVoid _void() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_null()
	 */
	@Override
	@Deprecated
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
	
	/****************************************************************/
	@Override
	public Object $$subContent() {
		 LinkedList<Object> ret = getListElem();
		 setListElem(new LinkedList<Object>());
		 return ret;
	}

	@Override
	public Object $$gosubContent(Object content) {
		LinkedList<Object> ret = getListElem();
		setListElem((LinkedList<Object>)content);
		return ret;
	}

	@Override
	public <E> E let(Class<? extends E >  type, Object name, Object... content) {
		_var(name,content);
		E v = declareType(type, name);
		return v;
	}
	
	@Override
	public <E> E let(String name, E content)
	{
		Class t = content.getClass();
		if (content instanceof Proxy)
		{
			MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(content);
			t = mh.getImplementClass();
		}
		return (E) let(t, name, content);
	}

	@Override
	public void let(JSMethodInterface name, Object... content) {
		_var(name, content);
	}
	
	@Override
	public void let(JSVariable name, Object... content){
		_var(name, content);
	}
	
	@Override
	public JSMethodInterface then(Anonym content) {

		try {
			content.run();
			MethodDesc currentMethodDesc = MethodInvocationHandler.ThreadLocalMethodDesc.get();
			MethodInvocationHandler.doLastSourceLineInsered(currentMethodDesc, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		endif();
		return this; 
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#_else(com.elisaxui.core.xui.xhtml.builder.javascript.Anonym)
	 */
	@Override
	public JSMethodInterface _else(Anonym content) {
		getListElem().add(" else {");
		getListElem().add(JSAddTab.class);
		
		try {
			content.run();
			MethodDesc currentMethodDesc = MethodInvocationHandler.ThreadLocalMethodDesc.get();
			MethodInvocationHandler.doLastSourceLineInsered(currentMethodDesc, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		endif();
		return this; 
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#_do(com.elisaxui.core.xui.xhtml.builder.javascript.Anonym)
	 */
	@Override
	public JSMethodInterface _do(Anonym content) {
		
		getListElem().add(JSAddTab.class);
		
		try {
			content.run();
			MethodDesc currentMethodDesc = MethodInvocationHandler.ThreadLocalMethodDesc.get();
			MethodInvocationHandler.doLastSourceLineInsered(currentMethodDesc, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListElem().add(JSRemoveTab.class);
		endfor();
		return this; 
	}





}