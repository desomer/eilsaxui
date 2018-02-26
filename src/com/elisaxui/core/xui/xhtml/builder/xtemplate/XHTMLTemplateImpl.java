/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class XHTMLTemplateImpl implements IXHTMLTemplate
{
	XMLElement xml;
	boolean modeJs = false;
	
	/**
	 * @return the modeJs
	 */
	public final boolean isModeJS() {
		return modeJs;
	}

	public static final JSFunction onEnter(Object row, XMLElement elem)
	{
		return (JSFunction) new JSFunction().setParam(new Object[] {row, "ctx"})
				._return( new XHTMLTemplateImpl(elem).setModeJS(true) );

	}
	
	public static final JSFunction onExit(Object row, Object dom)
	{
		return (JSFunction) new JSFunction().setParam(new Object[] {row, dom, "ctx"})
				._if(dom,"!=null")
					.__("$("+dom+").remove()")
				.endif();
	}
	
	public static final JSFunction onChange(JSChangeCtx ctx, JSDomElement aDom, JSFunction action )
	{
		return (JSFunction) new JSFunction().setParam(new Object[] {"ctx"})
				._var("dom" , "ctx.row['"+JSDataSet.ATTR_DOM_LINK+"']")
				._var("f", action.setParam(new Object[] {ctx, aDom}))
				.__("f(ctx, dom)")
				;		
	}
	
	/**
	 * @param modeJs the modeJs to set
	 */
	public final XHTMLTemplateImpl setModeJS(boolean modeJs) {
		this.modeJs = modeJs;
		return this;
	}

	public Object getContent() {
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);
		
		XMLBuilder elemJS =  new XMLBuilder("js", txtXML, txtXMLAfter);
		xml.setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine()+3);
		
		xml.toXML(elemJS.setModeString(!modeJs).setTemplate(modeJs));  // charge les string
		
		return this.modeJs?elemJS.getJSContent() :  "'"+txtXML.toString()+"'";
	}

	/**
	 * @param xml
	 */
	public XHTMLTemplateImpl(XMLElement xml) {
		super();
		this.xml = xml;
	}

	@Override
	public XMLElement getTemplate() {
		return null;
	}
	
	
}