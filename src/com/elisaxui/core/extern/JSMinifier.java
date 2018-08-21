/**
 * 
 */
package com.elisaxui.core.extern;

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
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;

/**
 * @author Bureau
 *
 */
public class JSMinifier {

	private JSMinifier() {
		super();
	}
	
	// https://developers.google.com/speed/docs/insights/MinifyResources
	
	public static final String optimizeJS(String code, boolean pretty) {

		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();
		
		options.setPrettyPrint(pretty);
		options.setRemoveDeadCode(true);
		
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

		SourceFile extern = SourceFile.fromCode("externs.js", "");
		SourceFile input = SourceFile.fromCode("input.js", code);

		String ret = code;
		Result result = compiler.compile(extern, input, options);
		if (result.success) {
			ret = compiler.toSource();
		}
		String start = "'use strict';";
		if (ret.startsWith(start))
			return ret.substring(start.length());
		else
			return ret;

		// /**
		// * @param code JavaScript source code to compile.
		// * @return The compiled version of the code.
		// */
		// Compiler compiler = new Compiler();
		//
		// CompilerOptions options = new CompilerOptions();
		//// Advanced mode is used here, but additional options could be set, too.
		// CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(
		// options);
		//
		// SourceFile extern = SourceFile.fromCode("externs.js",
		// "function alert(x) {}");
		//
		// SourceFile input = SourceFile.fromCode("input.js",
		// "function hello(name) {" +
		// "alert('Hello, ' + name);" +
		// "}" +
		// "hello('New user');");
		//
		// Result result = compiler.compile(extern, input, options);
		// if (result.success) {
		// System.out.println(compiler.toSource());
		// }

	}
	
	
//	public static final StringBuilder doMinifyJS(String js)
//	{
//		StringBuilder buf = new StringBuilder();
//	try {
//		CoreLogger.getLogger(1).info("call javascript-minifier");
//		
//		final URL url = new URL("https://javascript-minifier.com/raw");
//
//		// JS File you want to compress
//		byte[] bytes = js.getBytes( StandardCharsets.UTF_8);       
//		//Files.readAllBytes(Paths.get(""))
//
//		final StringBuilder data = new StringBuilder();
//		data.append(URLEncoder.encode("input", StandardCharsets.UTF_8.name()));
//		data.append('=');
//		data.append(URLEncoder.encode(new String(bytes), StandardCharsets.UTF_8.name()));
//
//		bytes = data.toString().getBytes(StandardCharsets.UTF_8);
//
//		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//		conn.setRequestMethod("POST");
//		conn.setDoOutput(true);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		conn.setRequestProperty("charset", StandardCharsets.UTF_8.name());
//		conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));
//
//		try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//		    wr.write(bytes);
//		}
//
//		final int code = conn.getResponseCode();
//
//		CoreLogger.getLogger(1).info(()->"Status: " + code);
//
//		if (code == 200) {
//		    final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		    String inputLine;
//
//		    while ((inputLine = in.readLine()) != null) {
//		    	buf.append(inputLine);
//		    }
//		    in.close();
//
//		} else {
//		    CoreLogger.getLogger(1).severe("erreur acces https://javascript-minifier.com/raw");
//		    buf.append(js);  // non minifier
//		}
//	} catch (IOException e) {
//		CoreLogger.getLogger(1).log(Level.SEVERE, "PB javascript-minifier.com",  e );
//	}
//	return buf;
//	}
	
}
