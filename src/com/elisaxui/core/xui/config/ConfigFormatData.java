/**
 * 
 */
package com.elisaxui.core.xui.config;

public class ConfigFormatData {
	public boolean minifyOnStart =false;
	public boolean enableCommentFctJS = !minifyOnStart;
	public boolean enableCrXML = !minifyOnStart;
	public boolean enableTabXML = !minifyOnStart;
	public boolean enableSpaceJS = !minifyOnStart;
	
	
	public boolean enableCrXMLinJS=false;
	public boolean enableTabXMLinJS=false;
	public boolean enableCrinJS=false;
	
	public boolean singlefile=false;
	
	public boolean reload = false;
	
	public void setMinify(boolean b)
	{
		enableCommentFctJS = !b;
		enableCrXML = !b;
		enableTabXML = !b;
		enableSpaceJS = !b;
		reload = true;
	}
	
}