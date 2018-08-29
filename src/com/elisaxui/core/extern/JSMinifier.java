/**
 * 
 */
package com.elisaxui.core.extern;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;

/**
 * @author Bureau
 *
 *	https://github.com/google/closure-compiler/wiki/Annotating-JavaScript-for-the-Closure-Compiler
 *  https://developers.google.com/speed/docs/insights/MinifyResources
 */
public class JSMinifier {

	private JSMinifier() {
		super();
	}
		
	public static final String optimizeJS(String code, boolean pretty) {

		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();
		
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
		options.setPrettyPrint(pretty);
		options.setRemoveDeadCode(true);
		options.setStrictModeInput(false);
		options.setLanguage(LanguageMode.ECMASCRIPT_2017);  //  LanguageMode.ECMASCRIPT_2018 ne fait rien
		
	
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

	}	
}
