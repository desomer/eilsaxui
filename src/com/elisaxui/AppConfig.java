/**
 * 
 */
package com.elisaxui;

import com.elisaxui.app.elisys.xui.page.AppRoot;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;

/**
 * @author gauth
 *
 */
public class AppConfig {

	
	public static String getRestPackage()
	{
		StringBuilder buf = new StringBuilder();
		String[] listePackage =  new String[] {"com.elisaxui.app.elisys.srv"};
		
		for (String string : listePackage) {
			if (buf.length()>0)
				buf.append(",");
			buf.append(string);
		}
		
		return buf.toString();
	}
	
	public static JSMethodInterface getJSApplication()
	{
		return new AppRoot().getJS();
	}
	
}
