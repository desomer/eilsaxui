/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.xui.XUIFactoryXHtml;

/**
 * @author Bureau pour le css et le html (JS grace a un ThreadLocal)
 */
public class XUIFormatManager {

	private double priority = 100.0;

	public double getPriority() {
		return priority;
	}

	public XUIFormatManager setPriority(double priority) {
		this.priority = priority;
		return this;
	}

	protected int nbTabInternal = 0;

	/**
	 * @return the nbTabInternal
	 */
	public final int getNbTabInternal() {
		return nbTabInternal;
	}

	/**
	 * @param nbTabInternal
	 *            the nbTabInternal to set
	 */
	public final void setNbTabInternal(int nbTabInternal) {
		this.nbTabInternal = nbTabInternal;
	}

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

		if (buf.isModeString()) {
			if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContentOnTarget("'+\n");
		} else {
			if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrXML())
				buf.addContentOnTarget("\n");
		}

		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableTabXML())
			for (int i = 0; i < nbTabForNewLine; i++) {
				buf.addContentOnTarget("\t");
			}

		if (buf.isModeString()) {
			if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrXMLinJS())
				buf.addContentOnTarget("'");
		}
	}

	/**
	 * ajout des tabulation apres un newLine
	 * @param buf
	 */
	public void newTabInternal(XMLBuilder buf) {
		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableTabXML())
			for (int i = 0; i < nbTabInternal; i++) {
				buf.addContentOnTarget("\t");
			}
	}

}