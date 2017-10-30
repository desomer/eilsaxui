/**
 * 
 */
package com.elisaxui;

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
	
		
}
