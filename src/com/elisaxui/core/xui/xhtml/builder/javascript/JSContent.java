package com.elisaxui.core.xui.xhtml.builder.javascript;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyMethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * super class de JSClass, JSFunction et proxy
 * herite de JSMethod
 * 
 * @author Bureau
 *
 */
public class JSContent implements IXMLBuilder, JSContentInterface {

	protected JSContentInterface proxy;
	
	/**
	 * @return the proxy
	 */
	public final JSContentInterface getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public final void setProxy(JSContentInterface proxy) {
		this.proxy = proxy;
	}

	/**************************************************************/
	// new line 
	public static interface JSNewLine {}
	public static interface JSAddTab {}
	public static interface JSRemoveTab {}
	/**************************************************************/
	
	/**
	 * @param jsBuilder
	 */
	public JSContent() {
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

	@Deprecated
	protected final void newLineForInternal(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrJS())
			buf.addContent("\n");
	}
	
	@Deprecated
	protected final void newTabForInternal(XMLBuilder buf) {
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
			ProxyHandler.getFormatManager().newLine(buf);
			ProxyHandler.getFormatManager().newTabInternal(buf);
		} else if (object == JSAddTab.class) {
			ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() + 1);
		} else if (object == JSRemoveTab.class) {
			ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() - 1);
		
		
		/***************************************************/
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
				ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() + 1);
				fct.toXML(buf);
				ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() - 1);
				ProxyHandler.getFormatManager().newLine(buf);
				ProxyHandler.getFormatManager().newTabInternal(buf);
			}
		} 
		else if (object instanceof JSVariable) {	
			Object v = ((JSVariable)object)._getValueOrName();
			if (v instanceof Array)
			{
				Array<?> arr = (Array<?>)v;
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
				Array<?> arr = (Array<?>)v;
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
		elem.setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine()+2);
		
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
		newLineForInternal(buf);
			for (int i = 0; i < ProxyHandler.getFormatManager().getTabForNewLine()+2; i++) {
				newTabForInternal(buf);
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
			ProxyHandler inv = (ProxyHandler) Proxy.getInvocationHandler(name);
			getListElem().add(textNew(inv.getImplementClass().getSimpleName(), ((JSListParameter) object).param));
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
	public JSContentInterface __(Object... content) {
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
	public JSContentInterface _set(Object name, Object... content) {
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
	public JSContentInterface _var(Object name, Object... content) {
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
	public JSContentInterface setTimeout(Object... content) {
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
	public JSContentInterface setTimeout(JSAnonym a, Object... content) {		
		getListElem().add(JSNewLine.class);
		getListElem().add("setTimeout(");
			addElem(callback(a));
			for (Object object : content) {
				addElem(",");
				addElem(object);
			}

		getListElem().add(");");
		return this;
	}
	
	@Override
	public JSContentInterface consoleDebug(Object... content) {
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
	public JSContentInterface systemDebugIf(Object cond, Object... content) {
		Array<Object> p = new Array<Object>();
		p.add("'<SYSTEM>'");
		p.addAll(Arrays.asList(content));
		
		return __(fragmentIf(cond).consoleDebug(p.toArray()));
	};

	
	/***************************************************************************/
	
	@Override
	public JSContentInterface _for(Object... content) {
		getListElem().add(JSNewLine.class);
		getListElem().add("for (");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(") {");
		return this;
	}

	@Override
	public JSContentInterface _forIdx(Object idx, JSArray array) {
		return _for("var "+idx+" = 0, "+idx+"len =", array.length(), "; "+idx+" < "+idx+"len; "+idx+"++");
	}
	
	@Override
	public JSContentInterface _forIdxBetween(JSInt idx, int start, int end) {
		return _for("var "+idx+" = "+start+";"+idx+" < "+end+"; "+idx+"++");
	}
	
	@Override
	public JSContentInterface endfor() {
		getListElem().add(JSNewLine.class);
		getListElem().add("}");
		return this;
	}

	/*****************************************************/
	@Override
	public JSContentInterface _if(Object... content) {
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
	public JSContentInterface _else() {
		getListElem().add(JSRemoveTab.class);
		getListElem().add(JSNewLine.class);
		getListElem().add("} else {");
		getListElem().add(JSAddTab.class);
		return this;
	}
	
	@Override
	public JSContentInterface _elseif_(Object... content) {
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
	public JSContentInterface _elseif(Object... content) {
		getListElem().add(" else if(");
		for (Object object : content) {
			addElem(object);
		}
		getListElem().add(") {");
		getListElem().add(JSAddTab.class);
		return this;
	}

	@Override
	public JSContentInterface endif() {
		getListElem().add(JSRemoveTab.class);
		getListElem().add(JSNewLine.class);
		getListElem().add("}");
		return this;
	}

	/********************************************************************************************/
	private final String textNew(String cl, Object...param ) {	
		StringBuilder buf = new StringBuilder();
		buf.append("new "+cl +"(");
		if (param!=null)
		{
			int i=0;
			for (Object object : param) {
				if (i>0)
					buf.append(", ");
				buf.append(object);
				i++;
			}
		}
		buf.append(")");
		return buf.toString() ;
	}

	@Deprecated
	@Override
	public Object _new(Object... param) {
		
		if (param.length>0 && param[0] instanceof Class)
			return ProxyHandler.cast((Class)param[0], textNew(((Class)param[0]).getSimpleName(), Arrays.copyOfRange(param, 1, param.length)));
		else
			return new JSListParameter(param);
	}
	
	@Override
	public <E> E newInst(Class<E> type, Object... param) {
		Object ret = JSClass.declareType(type, null);
		
		String t = type.getSimpleName();
		if (ret instanceof JSVariable)
			t= ((JSVariable) ret)._getClassType();
		
		String textNew = textNew( t, param);
		
		if (ret instanceof JSVariable)
			((JSVariable) ret)._setValue(textNew);
		else
			((JSClass) ret)._setContent(textNew);
		
		return (E)ret;
	}

	@Override
	public Object txt(Object... param) {
		
		StringBuilder str = new StringBuilder();
		str.append("\"");
		
		int l =param.length;
		boolean addEnd = true;
		
		for (int i = 0; i < l; i++) {
			if (param[i] instanceof JSVariable)
			{
				str.append("\"+");
				str.append(param[i]);
				
				if (i<l-1)
				{
					str.append("+\"");
				}
				else
					addEnd=false;
			}
			else
				str.append(param[i]);
		}
		if (addEnd)
			str.append("\"");
		
		return new JSString()._setValue(str.toString());
	}

	/***************************************************************/
	
	@Override
	public JSFunction fct(Object... param) {
		return new JSFunction().setParam(param);
	}
	
	@Override
	public JSFunction callback(JSAnonym call) {
		JSFunction ret =  new JSFunction();
		ret.proxy = (JSContentInterface) ProxyHandler.ThreadLocalMethodDesc.get().getProxy();
		ret.__(call);
		return ret;
	}
	
	@Override
	public JSFunction callback(Object param, JSAnonym call) {
		JSFunction ret =  new JSFunction();
		ret.proxy = (JSContentInterface) ProxyHandler.ThreadLocalMethodDesc.get().getProxy();
		ret.__(call);
		return ret;
	}
	
	@Override
	public JSFunction fragmentIf(Object cond) {
		
		return new JSFunction().setFragment(true).setActivatedCondition(cond);
	}

	/************************************************************************/
	
	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface#_return(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _return(Object... content) {
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
		List<Object> l = Arrays.asList(param);
		ArrayList<Object> l2 = new ArrayList<Object>();
		l2.add("(");
		l2.addAll(l);
		l2.add(")");
		
		return var(l2.toArray());
	}

	/**************************** FORMATAGE  ********************************/

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_void()
	 */
	@Override
	@Deprecated
	public JSVoid _void() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface#_null()
	 */
	@Override
	@Deprecated
	public JSContentInterface _null() {
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
	//	getListElem().add(0, JSAddTab.class);
	//	getListElem().add(JSRemoveTab.class);
		return ret;
	}

	@Override
	public <E> E let(Class<E>  type, Object name, Object... content) {
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
			ProxyHandler mh = (ProxyHandler) Proxy.getInvocationHandler(content);
			t = mh.getImplementClass();
		}
		return (E) let(t, name, content);
	}

	@Override
	public void let(JSContentInterface name, Object... content) {
		_var(name, content);
	}
	
	@Override
	public void let(JSVariable name, Object... content){
		_var(name, content);
	}
	
	@Override
	public JSContentInterface then(JSAnonym content) {

		try {
			content.run();
			ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();
			ProxyHandler.doLastSourceLineInsered(currentMethodDesc, true);
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
	public JSContentInterface _else(JSAnonym content) {
		getListElem().add(" else {");
		getListElem().add(JSAddTab.class);
		
		try {
			content.run();
			ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();
			ProxyHandler.doLastSourceLineInsered(currentMethodDesc, true);
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
	public JSContentInterface _do(JSAnonym content) {
		
		getListElem().add(JSAddTab.class);
		
		try {
			content.run();
			ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();
			ProxyHandler.doLastSourceLineInsered(currentMethodDesc, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListElem().add(JSRemoveTab.class);
		endfor();
		return this; 
	}





}