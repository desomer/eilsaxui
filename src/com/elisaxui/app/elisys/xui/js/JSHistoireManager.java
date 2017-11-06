/**
 * 
 */
package com.elisaxui.app.elisys.xui.js;

import com.elisaxui.app.elisys.xui.widget.JSSyllabisation;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;

/**
 * @author Bureau
 *
 */
public interface JSHistoireManager extends JSClass {
	
	JSSyllabisation jsSyllabe();
	
	default Object getHistoire()
	{
		JSon json = new JSon().setName("json");
		JSArray jsonSyllable = new JSArray().setName("jsonSyllable");

		Anonym onJson = (/*json*/)->{
			
			JSArray lesMots = new JSArray().setName("lesMots");
			JSInt i = new JSInt().setName("i");
			JSInt num = new JSInt().setName("num");
			
			var(lesMots, json.get("mots"));
			
			_forIdx(i, lesMots)
				.setTimeout( 
						fct(num).__(jsonSyllable.push(lesMots.at(num)))
					,"50+(20*i)", i)			
			.endfor();
			
			set("window.lastPhrase", json.get("text"));			
		};
		
		set(jsSyllabe(), "window.microlistener");
		var(jsonSyllable, jsSyllabe().aDataSet().getData());
		
		__("$.getJSON('"+JSSyllabisation.REST_JSON_SYLLABISATION+"').done(", fct(json).__(onJson) ,")");
				
		return _void();	
	}
	
}
