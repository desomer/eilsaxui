/**
 * 
 */
package com.elisaxui;

/**
 * @author gauth
 *
 */
public class AppConfig {

	/** TODO a mettre par introspection d'annotation */
	public static String getRestPackage()
	{
		StringBuilder buf = new StringBuilder();
		String[] listePackage =  new String[] {"com.elisaxui.app.elisys.srv", "com.elisaxui.app.elisys.xui.page.formation2"};
		
		for (String string : listePackage) {
			if (buf.length()>0)
				buf.append(",");
			buf.append(string);
		}
		
		return buf.toString();
	}
	
		
}
