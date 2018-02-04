/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.XUIFactoryXHtml;

/**
 * @author Bureau
 *    pour le css et le html    (JS grace a un ThreadLocal)
 */
public class XUIFormatManager {

	
	private double priority = 100.0;
	
	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	protected int nbTabInternal = 0;
	protected int nbTabForNewLine = 0;

	/**
	 * 
	 */
	public XUIFormatManager() {
		super();
	}

	public int getTabForNewLine() {
		return nbTabForNewLine;
	}

	public void setTabForNewLine(int nbInitialTab) {
		this.nbTabForNewLine = nbInitialTab;
	}


	
	public void newLine(XMLBuilder buf) {
	
		if (buf.isJS()) {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContent("'+\n");
		} else {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXML())
				buf.addContent("\n");
		}
		
		for (int i = 0; i < nbTabForNewLine; i++) {
			buf.addContent("\t");
		}
		
		if (buf.isJS()) {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContent("'");
		}
	}

	public void newTabInternal(XMLBuilder buf) {
		for (int i = 0; i < nbTabInternal; i++) {
			buf.addContent("\t");
		}
	}

}