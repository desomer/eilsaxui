/**
 * 
 */
package com.elisaxui.core.xui.config;

public class ConfigFormatData {
	private boolean minifyOnStart =false;
	private boolean enableCommentFctJS = !minifyOnStart;
	private boolean enableCrXML = !minifyOnStart;
	private boolean enableTabXML = !minifyOnStart;
	private boolean enableSpaceJS = !minifyOnStart;
	
	private boolean enableCrXMLinJS=false;
	private boolean enableTabXMLinJS=false;
	private boolean enableCrinJS=false;
	
	private boolean singlefile=false;
	private boolean es5=false;
	
	private boolean timeGenerated=true;
	private boolean fileChanged=true;
	private boolean patchChanges=true;
	
	
	/**
	 * @return the timeGenerated
	 */
	public final boolean isTimeGenerated() {
		return timeGenerated;
	}

	/**
	 * @param timeGenerated the timeGenerated to set
	 */
	public final void setTimeGenerated(boolean timeGenerated) {
		this.timeGenerated = timeGenerated;
	}

	/**
	 * @return the fileChanged
	 */
	public final boolean isFileChanged() {
		return fileChanged;
	}

	/**
	 * @param fileChanged the fileChanged to set
	 */
	public final void setFileChanged(boolean fileChanged) {
		this.fileChanged = fileChanged;
	}

	/**
	 * @return the patchChanges
	 */
	public final boolean isPatchChanges() {
		return patchChanges;
	}

	/**
	 * @param patchChanges the patchChanges to set
	 */
	public final void setPatchChanges(boolean patchChanges) {
		this.patchChanges = patchChanges;
	}

	private boolean reload = false;
	
	public void setMinify(boolean b)
	{
		minifyOnStart = b;
		enableCommentFctJS = !b;
		enableCrXML = !b;
		enableTabXML = !b;
		enableSpaceJS = !b;
		timeGenerated=!b;
		fileChanged=!b;
		patchChanges=!b;
	}

	/**
	 * @return the minifyOnStart
	 */
	public final boolean isMinifyOnStart() {
		return minifyOnStart;
	}

	/**
	 * @param minifyOnStart the minifyOnStart to set
	 */
	public final void setMinifyOnStart(boolean minifyOnStart) {
		this.minifyOnStart = minifyOnStart;
	}

	/**
	 * @return the enableCommentFctJS
	 */
	public final boolean isEnableCommentFctJS() {
		return enableCommentFctJS;
	}

	/**
	 * @param enableCommentFctJS the enableCommentFctJS to set
	 */
	public final void setEnableCommentFctJS(boolean enableCommentFctJS) {
		this.enableCommentFctJS = enableCommentFctJS;
	}

	/**
	 * @return the enableCrXML
	 */
	public final boolean isEnableCrXML() {
		return enableCrXML;
	}

	/**
	 * @param enableCrXML the enableCrXML to set
	 */
	public final void setEnableCrXML(boolean enableCrXML) {
		this.enableCrXML = enableCrXML;
	}

	/**
	 * @return the enableTabXML
	 */
	public final boolean isEnableTabXML() {
		return enableTabXML;
	}

	/**
	 * @param enableTabXML the enableTabXML to set
	 */
	public final void setEnableTabXML(boolean enableTabXML) {
		this.enableTabXML = enableTabXML;
	}

	/**
	 * @return the enableSpaceJS
	 */
	public final boolean isEnableSpaceJS() {
		return enableSpaceJS;
	}

	/**
	 * @param enableSpaceJS the enableSpaceJS to set
	 */
	public final void setEnableSpaceJS(boolean enableSpaceJS) {
		this.enableSpaceJS = enableSpaceJS;
	}

	/**
	 * @return the enableCrXMLinJS
	 */
	public final boolean isEnableCrXMLinJS() {
		return enableCrXMLinJS;
	}

	/**
	 * @param enableCrXMLinJS the enableCrXMLinJS to set
	 */
	public final void setEnableCrXMLinJS(boolean enableCrXMLinJS) {
		this.enableCrXMLinJS = enableCrXMLinJS;
	}

	/**
	 * @return the enableTabXMLinJS
	 */
	public final boolean isEnableTabXMLinJS() {
		return enableTabXMLinJS;
	}

	/**
	 * @param enableTabXMLinJS the enableTabXMLinJS to set
	 */
	public final void setEnableTabXMLinJS(boolean enableTabXMLinJS) {
		this.enableTabXMLinJS = enableTabXMLinJS;
	}

	/**
	 * @return the enableCrinJS
	 */
	public final boolean isEnableCrinJS() {
		return enableCrinJS;
	}

	/**
	 * @param enableCrinJS the enableCrinJS to set
	 */
	public final void setEnableCrinJS(boolean enableCrinJS) {
		this.enableCrinJS = enableCrinJS;
	}

	/**
	 * @return the singlefile
	 */
	public final boolean isSinglefile() {
		return singlefile;
	}

	/**
	 * @param singlefile the singlefile to set
	 */
	public final void setSinglefile(boolean singlefile) {
		this.singlefile = singlefile;
	}

	/**
	 * @return the es5
	 */
	public final boolean isEs5() {
		return es5;
	}

	/**
	 * @param es5 the es5 to set
	 */
	public final void setEs5(boolean es5) {
		this.es5 = es5;
	}

	/**
	 * @return the reload
	 */
	public final boolean isReload() {
		return reload;
	}

	/**
	 * @param reload the reload to set
	 */
	public final void setReload(boolean reload) {
		this.reload = reload;
	}
	
}