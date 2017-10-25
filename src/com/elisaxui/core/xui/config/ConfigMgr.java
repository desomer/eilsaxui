package com.elisaxui.core.xui.config;

public class ConfigMgr {
	private boolean enableCrXML = true;   //true
	private boolean enableTabXML = false;
	private boolean enableCrXMLinJS = true;   //true
	private boolean enableTabXMLinJS = false;
	private boolean enableCrJS = true;    //true
	
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
		return enableCrJS;
	}
	public void setEnableCrJS(boolean enableCrJS) {
		this.enableCrJS = enableCrJS;
	}
}
