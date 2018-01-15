/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * @author Bureau
 *
 */
public class JSMinifier {

	private static class YuiCompressorErrorReporter implements ErrorReporter {

		@Override
		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
			// TODO Auto-generated method stub
			System.out.println(message+ ">"+ line + ">"+ lineSource);
		}


		@Override
		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			error(message, sourceName, line, lineSource, lineOffset);
	        return new EvaluatorException(message);
		}

		/* (non-Javadoc)
		 * @see org.mozilla.javascript.ErrorReporter#warning(java.lang.String, java.lang.String, int, java.lang.String, int)
		 */
		@Override
		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			// TODO Auto-generated method stub
			System.out.println(message);
		}
		
	}
	
	private static class Options {
	    public String charset = "UTF-8";
	    public int lineBreakPos = -1;
	    public boolean munge = false;
	    public boolean verbose = true;
	    public boolean preserveAllSemiColons = true;
	    public boolean disableOptimizations = true;
	}
	
	public static final StringBuilder doMinifyJS2(String js)
	{
		Options o =new Options();
		
		StringReader reader = new StringReader(js);
		StringWriter out = new StringWriter(js.length());
		try {
			JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new YuiCompressorErrorReporter());
			
			compressor.compress(out, o.lineBreakPos, o.munge, o.verbose, o.preserveAllSemiColons, o.disableOptimizations);
			
			
		} catch (EvaluatorException | IOException e) {
			e.printStackTrace();
		}
		
		return new StringBuilder( out.getBuffer());
	}
	
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
		e.printStackTrace();
	}
	return buf;
	}
	
}
