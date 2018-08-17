/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import com.elisaxui.ResourceLoader;
import com.elisaxui.core.helper.URLLoader;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;

/**
 * @author gauth
 *
 */
public interface IResourceLoader {

	default String loadResource(String id, boolean mini) {

		Class<?> cl = this.getClass();

		boolean retJSClass = JSClass.class.isAssignableFrom(cl);
		if (retJSClass) {
			ProxyHandler mh = (ProxyHandler) Proxy.getInvocationHandler(this);
			cl = mh.getImplementClass();
		}

		URL url = ResourceLoader.getResource(new ResourceLoader(cl, id));

		String str = null;
		try {
			str = new String(Files.readAllBytes(Paths.get(url.toURI())));
		} catch (IOException | URISyntaxException e) {
			CoreLogger.getLogger(1).log(Level.SEVERE, "Pb loadResource " + id, e);
		}

		return mini ? minifyJS(str, false) : str;
	}

	default String loadResourceFromURL(String u) {
		return minifyJS(URLLoader.loadTextFromUrl(u).toString(), false);
	}

	// https://developers.google.com/speed/docs/insights/MinifyResources

	default String minifyJS(String code, boolean pretty) {

		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();

		CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);

		options.setPrettyPrint(pretty);

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

}
