package com.elisaxui.core.xui.config;

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
		return data.singlefile;
	}
	public final boolean isEnableSpaceJS() {
		return data.enableSpaceJS;
	}
	public boolean isEnableCommentFctJS() {
		return data.enableCommentFctJS;
	}
	public boolean isEnableCrXML() {
		return data.enableCrXML;
	}
	public boolean isEnableTabXML() {
		return data.enableTabXML;
	}
	public boolean isEnableCrXMLinJS() {
		return data.enableCrXMLinJS;
	}
	public boolean isEnableTabXMLinJS() {
		return data.enableTabXMLinJS;
	}
	public boolean isEnableCrJS() {
		return data.enableCrinJS;
	}
}
