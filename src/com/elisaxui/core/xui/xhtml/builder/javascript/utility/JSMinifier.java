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
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.elisaxui.core.helper.log.CoreLogger;

/**
 * @author Bureau
 *
 */
public class JSMinifier {

	private JSMinifier() {
		super();
	}

	
	public static final StringBuilder doMinifyJS(String js)
	{
		StringBuilder buf = new StringBuilder();
	try {
		CoreLogger.getLogger(1).info("call javascript-minifier");
		
		final URL url = new URL("https://javascript-minifier.com/raw");

		// JS File you want to compress
		byte[] bytes = js.getBytes( StandardCharsets.UTF_8);       
		//Files.readAllBytes(Paths.get(""))

		final StringBuilder data = new StringBuilder();
		data.append(URLEncoder.encode("input", StandardCharsets.UTF_8.name()));
		data.append('=');
		data.append(URLEncoder.encode(new String(bytes), StandardCharsets.UTF_8.name()));

		bytes = data.toString().getBytes(StandardCharsets.UTF_8);

		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", StandardCharsets.UTF_8.name());
		conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));

		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
		    wr.write(bytes);
		}

		final int code = conn.getResponseCode();

		CoreLogger.getLogger(1).info(()->"Status: " + code);

		if (code == 200) {
		    final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;

		    while ((inputLine = in.readLine()) != null) {
		    	buf.append(inputLine);
		    }
		    in.close();

		} else {
		    CoreLogger.getLogger(1).severe("erreur acces https://javascript-minifier.com/raw");
		    buf.append(js);  // non minifier
		}
	} catch (IOException e) {
		CoreLogger.getLogger(1).log(Level.SEVERE, "PB javascript-minifier.com",  e );
	}
	return buf;
	}
	
}
