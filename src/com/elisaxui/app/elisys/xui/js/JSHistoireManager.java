/**
 * 
 */
package com.elisaxui.app.elisys.xui.js;

import com.elisaxui.app.elisys.xui.page.main.widget.JSSyllabisation;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;

/**
 * @author Bureau
 *
 * gestion de l'histoire manager 
 * 	- appel api 
 */
public interface JSHistoireManager extends JSClass {
	
	default Object getHistoire()
	{
		JPhrase aJPhrase = declareType(JPhrase.class, "aJPhrase"); 
		JSArray jsonSyllable = declareType(JSArray.class, "jsonSyllable"); 
		JSSyllabisation jsSyllabe = declareType(JSSyllabisation.class, "jsSyllabe"); 

		_var(jsSyllabe, "window.microlistener");
		_var(jsonSyllable, jsSyllabe.aDataSet().getData());
		
		// TODO   JSArray jsonSyllable =  let(jsSyllabe.aDataSet().getData())
					// jsonSyllable = aJSArray1
			
		
		JSFunction onJson = funct(aJPhrase).__(()->{
			
			JSInt i = declareType(JSInt.class,"i"); 
			JSInt num = declareType(JSInt.class,"num");
			
			_forIdx(i, aJPhrase.mots())._do(()->{
				setTimeout( 
						funct(num).__(jsonSyllable.push(aJPhrase.mots().at(num)))
					,"50+(20*i)", i);	
			});
			
			_set("window.lastPhrase", aJPhrase.text());			
		});
		
		__("$.getJSON('"+JSSyllabisation.REST_JSON_SYLLABISATION+"').done(", onJson ,")");
				
		return _void();	
	}
	
}
