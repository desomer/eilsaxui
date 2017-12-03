/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.XUIFactoryXHtml;

/**
 * @author Bureau
 *
 */
public class XUIFormatManager {

	
	private int priority = 100;
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	protected int nbTabInternal = 0;
	protected int nbInitialTab = 0;

	/**
	 * 
	 */
	public XUIFormatManager() {
		super();
	}

	public int getNbInitialTab() {
		return nbInitialTab;
	}

	public void setNbInitialTab(int nbInitialTab) {
		this.nbInitialTab = nbInitialTab;
	}


	
	public void newLine(XMLBuilder buf) {
	
		if (buf.isJS()) {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContent("'+\n");
		} else {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXML())
				buf.addContent("\n");
		}
	
		for (int i = 0; i < nbInitialTab; i++) {
			buf.addContent("\t");
		}
		
		if (buf.isJS()) {
			if (XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContent("'");
		}
	}

	public void newTabulation(XMLBuilder buf) {
		// if (buf.isJS()) return;
		for (int i = 0; i < nbTabInternal; i++) {
			buf.addContent("\t");
		}
	}

}