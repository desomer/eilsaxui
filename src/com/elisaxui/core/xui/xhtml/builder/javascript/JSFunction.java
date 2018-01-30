package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.util.concurrent.Callable;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * class representant une function JS
 * @author Bureau
 *
 */
public class JSFunction extends JSContent
{
	//"use strict"
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder strBuf = new StringBuilder();
		XMLBuilder buf = new XMLBuilder("????", strBuf, null);
		this.toXML(buf);
		return strBuf.toString();
	}


	Object name = null;
	Object[] param = null; 
	JSContent code;
	boolean isFragment = false;
	Object activeCondition = null;
	
	public Object getActivatedCondition() {
		return activeCondition;
	}

	public JSFunction setActivatedCondition(Object activated) {
		this.activeCondition = activated;
		return this;
	}

	public boolean isActived()
	{
		if (activeCondition instanceof Boolean)
		{
			boolean b = (boolean)activeCondition;
			if (b==false)
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

	public JSMethodInterface getCode() {
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

	public JSFunction __( Anonym c)
	{
		JSBuilder.doFctAnonym(this , c);
		return this;
	}
	
	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		if (!isFragment) {
			if (name==null)
			{
				if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS())
					buf.addContent("/******** anonymous *******/");
				buf.addContent("function");
			}
			else
			{
				jsBuilder.newTabulation(buf);
				if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS())
					buf.addContent("/******** start "+ name + " *******/");
				jsBuilder.newLine(buf);
				jsBuilder.newTabulation(buf);
				buf.addContent(name);
			}
			buf.addContent("(");
			if (param!=null) {
				for (int i = 0; i < param.length; i++) {
					if (i > 0)
						buf.addContent(", ");
					buf.addContent(param[i]);
				}
			}
			buf.addContent(")");
			buf.addContent(" {");
			
			jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()+1);
	    }
		else
		{
			if (!isActived())
				return buf;
		}
	
		if (code!=null)
			code.toXML(buf);
		else
			super.toXML(buf);
		
		if (!isFragment)
		{
			jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()-1);
			jsBuilder.newLine(buf);
			jsBuilder.newTabulation(buf);
			buf.addContent("}");
			if (name==null)
			{
				jsBuilder.newLine(buf);
				jsBuilder.newTabulation(buf);
				if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS())
					buf.addContent("/******** end anonymous *******/");
			}
		}
		
		return buf;
	}

}
