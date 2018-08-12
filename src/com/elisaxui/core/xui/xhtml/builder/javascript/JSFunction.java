package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.util.logging.Level;

import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyMethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * class representant une function JS
 * 
 * @author Bureau
 *
 */
public class JSFunction extends JSContent implements JSElement {
	// "use strict"

	@Override
	public String toString() {
		StringBuilder strBuf = new StringBuilder();
		XMLBuilder buf = new XMLBuilder("????", strBuf, null);
		if (isDebug())
			debug=true;
		
		this.toXML(buf);
		return strBuf.toString();
	}

	Object name = null;
	Object[] param = null;
	JSContent code;
	boolean isFragment = false;
	boolean isStatic = false;
	Object comment = null;
	boolean debug = false;
	
	Object bind = null;
	int numLine = 0;
	
	/**
	 * @return the numLine
	 */
	public final int getNumLine() {
		return numLine;
	}
	
	public JSCallBack toCallBack()
	{
		return cast(JSCallBack.class, this);
	}

	/**
	 * @param numLine the numLine to set
	 */
	public final JSFunction setNumLine(int numLine) {
		this.numLine = numLine;
		return this;
	}

	/**
	 * @return the debug
	 */
	public final boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public final void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return the comment
	 */
	private final Object getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public final JSFunction setComment(Object comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * @return the isStatic
	 */
	public final boolean isStatic() {
		return isStatic;
	}

	/**
	 * @param isStatic the isStatic to set
	 */
	public final JSFunction setStatic(boolean isStatic) {
		this.isStatic = isStatic;
		return this;
	}

	Object activeCondition = null;

	public Object getActivatedCondition() {
		return activeCondition;
	}

	public JSFunction setActivatedCondition(Object activated) {
		this.activeCondition = activated;
		return this;
	}

	public boolean isActived() {
		if (activeCondition instanceof Boolean) {
			boolean b = (boolean) activeCondition;
			if (!b)
				return false;
		}
		return true;
	}

	public boolean isFragment() {
		return isFragment;
	}

	public JSFunction setFragment(boolean isFragment) {
		this.isFragment = isFragment;
		return this;
	}

	public JSContentInterface getCode() {
		return code;
	}

	public JSFunction setCode(JSContent code) {
		this.code = code;
		return this;
	}

	public Object getName() {
		return name;
	}

	public JSFunction setName(Object name) {
		this.name = name;
		return this;
	}

	public Object[] getParam() {
		return param;
	}

	public JSFunction setParam(Object[] param) {
		this.param = param;
		return this;
	}
	
	
	public JSFunction bind(Object obj)
	{
		bind = obj;
		return this;
	}
	

	public JSFunction()
	{
		try {
			ProxyMethodDesc m = ProxyHandler.getMethodDescFromStacktrace();
			
			if (m.lastMthNoInserted!=null)
			{
				String classMeth = m.lastMthNoInserted.toString();
				setComment("anonym " + classMeth.substring(classMeth.lastIndexOf('.')+1)+".java:"+m.lastLineNoInsered);
			}		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSFunction(String classe, String name)
	{		
		setName(name);
		setComment(classe);
	}
	
	
	public JSFunction __(JSLambda c) {
		doFctAnonym(c);
		return this;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {

		if (isDebug())
			debug=true;
		
		Object comment = null;
		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCommentFctJS())
		{
			comment = getComment();
			if (comment!=null && comment.toString().length()==0)
				comment=null;
			else
				comment = (comment==null?"anonymous":comment);
		}
		
		if (!isFragment) {
			if (name == null) {
				buf.addContentOnTarget("function");
			} else {
				ProxyHandler.getFormatManager().newTabInternal(buf);
				if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCommentFctJS())
					buf.addContentOnTarget("/******** " + name +" (" + comment+") *******/");
				ProxyHandler.getFormatManager().newLine(buf);
				ProxyHandler.getFormatManager().newTabInternal(buf);
				if (this.isStatic)
					buf.addContentOnTarget("static ");	
				buf.addContentOnTarget(name);
			}
			buf.addContentOnTarget("(");
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					if (i > 0)
						buf.addContentOnTarget(", ");
					buf.addContentOnTarget(param[i]);
				}
			}
			buf.addContentOnTarget(")");
			buf.addContentOnTarget(" {");
			
			if (!isFragment && name == null) {
				if (comment!=null)
					buf.addContentOnTarget("/*** start "+comment+ " ***/");
			}

			ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() + 1);
		} else {
			if (!isActived())
				return buf;
		}

		if (code != null) {
			code.toXML(buf); // code de la fonction js
		} else
			super.toXML(buf);

		if (!isFragment) {
			ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine() - 1);
			ProxyHandler.getFormatManager().newLine(buf);
			ProxyHandler.getFormatManager().newTabInternal(buf);
			buf.addContentOnTarget("}");
			if (bind!=null)
			{
				buf.addContentOnTarget(".bind(");
				buf.addContentOnTarget(bind);
				buf.addContentOnTarget(")");
			}
			// fin anomyn
			if (name == null) {
				if (comment!=null)
					buf.addContentOnTarget("/*** end " +comment+ " ***/");
			}
		}
		
		return buf;
	}

	public void doFctAnonym(Runnable c) {
		Object ret = proxy.$$subContent();

		try {
			c.run();
			// insere la dernier ligne
			ProxyHandler.doLastSourceLineInsered(true);
		} catch (Exception e) {
			CoreLogger.getLogger(3).log(Level.SEVERE, "pb run anonym", e);
		}

		Object sub = proxy.$$gosubContent(ret);
		JSContent cont = new JSContent();
		cont.$$gosubContent(sub);
		setCode(cont);
	}

}
