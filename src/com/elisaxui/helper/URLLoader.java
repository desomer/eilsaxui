/**
 * 
 */
package com.elisaxui.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Bureau
 *
 */
public class URLLoader {

	
	public static final StringBuilder loadUrl(String u)
	{
	    URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    StringBuilder buf = new StringBuilder();
	    try {
	       url = new URL(u);
	       is = url.openStream();
	       br = new BufferedReader(new InputStreamReader(is));
	       
	       while ((line = br.readLine()) != null) {
	    	   buf.append(line);
	    	   buf.append("\n");
	       } 
	    } catch (MalformedURLException mue) {
	       mue.printStackTrace();
	    } catch (IOException ioe) {
	       ioe.printStackTrace();
	    } 
	    finally {
	       try {
	          if (is != null) is.close();
	       } catch (IOException ioe) {} 
	    } 
	    
	    return buf;
	}
}
