/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Bureau
 *
 */
public class JSMinifier {

	public static final StringBuilder doMinifyJS(String js)
	{
		StringBuilder buf = new StringBuilder();
	try {
		System.out.print("javascript-minifier => ");
		
		final URL url = new URL("https://javascript-minifier.com/raw");

		// JS File you want to compress
		byte[] bytes = js.getBytes( "UTF-8");        //Files.readAllBytes(Paths.get(""));

		final StringBuilder data = new StringBuilder();
		data.append(URLEncoder.encode("input", "UTF-8"));
		data.append('=');
		data.append(URLEncoder.encode(new String(bytes), "UTF-8"));

		bytes = data.toString().getBytes("UTF-8");

		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
		    wr.write(bytes);
		}

		final int code = conn.getResponseCode();

		System.out.println("Status: " + code);

		if (code == 200) {
		   // System.out.println("----");
		    final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;

		    while ((inputLine = in.readLine()) != null) {
		    	buf.append(inputLine);
		    }
		    in.close();

		  //  System.out.println("\n----");
		} else {
		    System.out.println("erreur acces https://javascript-minifier.com/raw");
		    return null;
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return buf;
	}
	
}
