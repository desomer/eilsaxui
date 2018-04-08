package com.elisaxui.core.xui.config;

public class ConfigFormat {
	private boolean minify = false;
	
	private boolean enableCommentFctJS = !minify;    //===================>true
	private boolean enableCrXML = !minify;   //===============> true
	private boolean enableTabXML = !minify;   //===============> true
	private boolean enableSpaceJS = !minify;   //===============> true
	
	private boolean enableCrXMLinJS = true;   //true
	private boolean enableTabXMLinJS = false; //false
	private boolean enableCrinJS = true;    //true
	
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
	 * @return the enableCommentFctJS
	 */
	public boolean isEnableCommentFctJS() {
		return enableCommentFctJS;
	}
	/**
	 * @param enableCommentFctJS the enableCommentFctJS to set
	 */
	public void setEnableCommentFctJS(boolean enableCommentFctJS) {
		this.enableCommentFctJS = enableCommentFctJS;
	}
	public boolean isEnableCrXML() {
		return enableCrXML;
	}
	public void setEnableCrXML(boolean enableCrXML) {
		this.enableCrXML = enableCrXML;
	}
	public boolean isEnableTabXML() {
		return enableTabXML;
	}
	public void setEnableTabXML(boolean enableTabXML) {
		this.enableTabXML = enableTabXML;
	}
	public boolean isEnableCrXMLinJS() {
		return enableCrXMLinJS;
	}
	public void setEnableCrXMLinJS(boolean enableCrXMLinJS) {
		this.enableCrXMLinJS = enableCrXMLinJS;
	}
	public boolean isEnableTabXMLinJS() {
		return enableTabXMLinJS;
	}
	public void setEnableTabXMLinJS(boolean enableTabXMLinJS) {
		this.enableTabXMLinJS = enableTabXMLinJS;
	}
	public boolean isEnableCrJS() {
		return enableCrinJS;
	}
	public void setEnableCrJS(boolean enableCrJS) {
		this.enableCrinJS = enableCrJS;
	}
}
