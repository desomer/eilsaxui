package com.elisaxui.core.xui.app;

public class ConfigFormat {
	private static ConfigFormatData data = new ConfigFormatData();

	/**
	 * @return the data
	 */
	public static final ConfigFormatData getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public static final void setData(ConfigFormatData d) {
		data = d;
	}
	/**
	 * @return the singlefile
	 */
	public final boolean isSinglefile() {
		return data.isSinglefile();
	}
	public final boolean isEnableSpaceJS() {
		return data.isEnableSpaceJS();
	}
	public boolean isEnableCommentFctJS() {
		return data.isEnableCommentFctJS();
	}
	public boolean isEnableCrXML() {
		return data.isEnableCrXML();
	}
	public boolean isEnableTabXML() {
		return data.isEnableTabXML();
	}
	public boolean isEnableCrXMLinJS() {
		return data.isEnableCrXMLinJS();
	}
	public boolean isEnableTabXMLinJS() {
		return data.isEnableTabXMLinJS();
	}
	public boolean isEnableCrJS() {
		return data.isEnableCrinJS();
	}
}
