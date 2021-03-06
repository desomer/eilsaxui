/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.template;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 * class representant un template dom   
 * a retirer apres le retrait du dom text JSXHTMLPart du paquet template
 * car peux etre fait en automatique si le fct append child recupere un xDiv en parametre 
 */
public class JSNodeTemplate extends JSNodeElement implements IXHTMLBuilder
{
	
	XMLElement xml;
	boolean modeJs = false;
	
	/**
	 * @return the modeJs
	 */
	public final boolean isModeJS() {
		return modeJs;
	}

	/**
	 * @param modeJs the modeJs to set
	 */
	public final JSNodeTemplate setModeJS(boolean modeJs) {
		this.modeJs = modeJs;
		return this;
	}

	public Object getContent() {
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);
		
		XMLBuilder elemJS =  new XMLBuilder("js", txtXML, txtXMLAfter);
		xml.setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine());
		xml.setNbTabInternal(ProxyHandler.getFormatManager().getNbTabInternal());
		
		xml.toXML(elemJS.setModeString(!modeJs).setModeTemplate(modeJs));  // mode template et pas les vieux string
		
		return this.modeJs?elemJS.getJSContent() :  "'"+txtXML.toString()+"'";
	}

	/**
	 * @param xml
	 */
	public JSNodeTemplate(XMLElement xml) {
		super();
		this.xml = xml;
	}
	
	public JSNodeTemplate()
	{
		super();
	}
			
		
}