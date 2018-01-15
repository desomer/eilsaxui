/**
 * 
 */
package com.elisaxui.core.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.eclipsesource.v8.V8;
import com.elisaxui.ResourceLoader;
import com.elisaxui.app.elisys.xui.asset.AssetHandler;

/**
 * @author gauth
 *
 */
public class JSExecutorHelper {

	static Invocable doBabel = null;
	static Bindings bindings = null;
	
	private static final ThreadLocal<V8> ThreadLocalV8 = new ThreadLocal<V8>();
	public static final boolean VERSION_V8 = true;
	
	
	public static final boolean WITH_BABEL = false;

	
	public static void initGlobal() throws ScriptException, IOException, NoSuchMethodException
	{
		 
		if (!WITH_BABEL)
			return;
		
		
		if (VERSION_V8) {
//			 System.out.println("----------- START BABEL------------------");
//			 runtimeV8 = V8.createV8Runtime();
//			 Object result = runtimeV8.executeScript(script);
//			 System.out.println(result);
//			 System.out.println("-----------------------------------");
		}
		else 
		{
		
			System.out.println("----------- START BABEL------------------");

	        ScriptEngine engine =  new ScriptEngineManager().getEngineByName("nashorn");   //JavaScript  nashorn
	        Compilable compilingEngine = (Compilable) engine;
	        System.out.println("----------- COMPIL BABEL------------------");
	        String script = getScriptBabel();
	        CompiledScript cscript = compilingEngine.compile(script);
	        bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
//	        for(Map.Entry me : bindings.entrySet()) {
//	            System.out.printf("%s: %s\n",me.getKey(),String.valueOf(me.getValue()));
//	        }
	        cscript.eval(bindings);
	        System.out.println("-----------------------------------");
	        doBabel = (Invocable) cscript.getEngine();
		}
	        
//	        Object ret = invocable.invokeFunction("doBabel");
//	        System.out.println(ret);
	        
	        
	 //       System.out.println(ret);
		
//		ScriptEngineManager engineManager =  new ScriptEngineManager();
//		ScriptEngine engine = 	 engineManager.getEngineByName("nashorn");
////		engine.eval("function sum(a, b) { return a + b; }");
////		System.out.println(engine.eval("sum(1, 2);"));
//		
//		URL babelURL = ResourceLoader.getResource(AssetHandler.dicoAsset.get("babel"));
//		BufferedReader in = new BufferedReader(new InputStreamReader(babelURL.openStream()));
//		//engine.eval(in);
//		String response = new String();
//	//	for (String line; (line = in.readLine()) != null; response += line);
//		
//		response += "function doBabel(input) { return Babel.transform(input, { presets: ['es2015'] }).code; }";
//
//		
//		Compilable compilingEngine = (Compilable) engine;
//		CompiledScript compiledJS = compilingEngine.compile(response);
//		
//		Bindings bindings = compiledJS.
//        for(Map.Entry me : bindings.entrySet()) {
//            System.out.printf("%s: %s\n",me.getKey(),String.valueOf(me.getValue()));
//        }
//        bindings.put("input", "");
//        //cscript.eval();
//       Object obj = compiledJS.eval(bindings);
//		
////		Bindings bindings = compiledJS.getEngine().createBindings();
////		// Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
////	        for(Map.Entry me : bindings.entrySet()) {
////	            System.out.printf("%s: %s\n",me.getKey(),String.valueOf(me.getValue()));
////	        }
//		
//	//	SimpleBindings sb = new SimpleBindings();
//	//	Object result =  compiledJS.eval(sb);
//		
//			babelEngine=compiledJS.getEngine();	
//		
//		//
//		//System.out.println();
	}


	/**
	 * @return
	 * @throws IOException
	 */
	private static String getScriptBabel() throws IOException {
		URL babelURL = ResourceLoader.getResource(AssetHandler.dicoAsset.get("babel"));
		BufferedReader in = new BufferedReader(new InputStreamReader(babelURL.openStream()));
		//engine.eval(in);
		String script = new String();
		for (String line; (line = in.readLine()) != null; script += line);
		
		
		if (VERSION_V8)
			script += "function doBabel(input) { return Babel.transform(input, { presets: ['es2015'], minified: false, comments: false}).code; }";
		else
			script += "function doBabel() { return Babel.transform(input, { presets: ['es2015'] }).code; }";
		return script;
	}
	
	
	public static void initThread()
	{
		// TODO  utiliser un WebWorker
		// https://github.com/irbull/j2v8_examples/blob/master/ThreadedMergeSort/src/com/ianbull/j2v8_examples/webworker/WebWorker.java
		
		
		if (!WITH_BABEL)
			return;
		
		if (VERSION_V8) {
			 System.out.println("----------- START BABEL------------------");
			 V8 runtimeV8 =  V8.createV8Runtime();
			 ThreadLocalV8.set(runtimeV8);
			 Object result=null;
			 try {
				result = runtimeV8.executeScript(getScriptBabel());
			 } catch (IOException e) {
				e.printStackTrace();
			 }
			 System.out.println(result);
			 System.out.println("-----------------------------------");
		}
	}
	
	public static void stopThread()
	{
		if (!WITH_BABEL)
			return;
		
		if (VERSION_V8) {
			V8 runtimeV8 = ThreadLocalV8.get();
			runtimeV8.release();
		}
	}
	
	public static String doBabel(String str) throws ScriptException, NoSuchMethodException
	{
		if (VERSION_V8) {
			V8 runtimeV8 = ThreadLocalV8.get();
			return ""+ runtimeV8.executeJSFunction("doBabel", str);
		} else
		{
			bindings.put("input", str);
			return ""+ doBabel.invokeFunction("doBabel");
		}
		
	}
}
