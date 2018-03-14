/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class JSDomTemplate extends JSDomElement implements IJSDomTemplate
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
	public final JSDomTemplate setModeJS(boolean modeJs) {
		this.modeJs = modeJs;
		return this;
	}

	public Object getContent() {
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);
		
		XMLBuilder elemJS =  new XMLBuilder("js", txtXML, txtXMLAfter);
		xml.setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine()+3);
		
		xml.toXML(elemJS.setModeString(!modeJs).setModeTemplate(modeJs));  // charge les string
		
		return this.modeJs?elemJS.getJSContent() :  "'"+txtXML.toString()+"'";
	}

	/**
	 * @param xml
	 */
	public JSDomTemplate(XMLElement xml) {
		super();
		this.xml = xml;
	}

	@Override
	public XMLElement getTemplate() {
		return null;
	}
	
	
}