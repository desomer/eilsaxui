/**
 *    https://vuejs-tips.github.io/compiler/
 *   
 */
package com.elisaxui.core.extern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

public class JSExeHelper {

    // my javascript beautifier of choice
    private static final String BEAUTIFY_JS_RESOURCE = "beautify.js";
    private static final String VUE_JS_RESOURCE = "vue.js";
    private static final String VUE_COMPILER_JS_RESOURCE = "vue.compiler.js";
    
    // name of beautifier function
    private static final String BEAUTIFY_METHOD_NAME = "js_beautify";
    private static final String COMPILE_METHOD_NAME = "compileVueJs";
    
    /***************************************************/
    static private ScriptEngine engine = null;

    public static synchronized void init() throws ScriptException {
    	if (engine==null)
    	{
	        engine = new ScriptEngineManager().getEngineByName("nashorn");
	
	        engine.eval("var global = this;");
	        engine.eval(new InputStreamReader(JSExeHelper.class.getResourceAsStream(BEAUTIFY_JS_RESOURCE)));
	        engine.eval(new InputStreamReader(JSExeHelper.class.getResourceAsStream(VUE_JS_RESOURCE)));
	        engine.eval(new InputStreamReader(JSExeHelper.class.getResourceAsStream(VUE_COMPILER_JS_RESOURCE)));
    	}
    }

    public String beautify(String javascriptCode) throws ScriptException, NoSuchMethodException {
        return (String) ((Invocable) engine).invokeFunction(BEAUTIFY_METHOD_NAME, javascriptCode);
    }
    
    public String compileVueJs(String template) throws ScriptException, NoSuchMethodException {
        return (String) ((Invocable) engine).invokeFunction(COMPILE_METHOD_NAME, template);
    }
    

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
    	JSExeHelper.init();
    	
        String unformattedJs = "var a = 1; b = 2; var user = { name : \n \"Andrew\"}";

        JSExeHelper executor = new JSExeHelper();
        String formattedJs = executor.beautify(unformattedJs);
        System.out.println(formattedJs);
        
        String template = "<p>Le message est : {{ message }}</p>";
        String compilVue = executor.compileVueJs(template);
        
        System.out.println(compilVue);
        
    }
    
     

}