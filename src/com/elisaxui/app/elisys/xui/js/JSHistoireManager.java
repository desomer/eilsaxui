/**
 * 
 */
package com.elisaxui.app.elisys.xui.js;

import com.elisaxui.app.elisys.xui.widget.JSSyllabisation;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;

/**
 * @author Bureau
 *
 */
public interface JSHistoireManager extends JSClass {
	
	JSSyllabisation _jsSyllabe = null;
	
	default Object getHistoire()
	{
		JSon json = new JSon().setName("json");
		JSArray lesMots = new JSArray().setName("lesMots");
		JSArray jsonSyllable = new JSArray().setName("jsonSyllable");
		
		Anonym onJson = ()->{
			var(lesMots, json.attr("mots"));
			
			_forIdx("i", lesMots)
				.__("setTimeout(", fct("num")
						.__(jsonSyllable.push(lesMots.get("num")))
					, ",50+(20*i), i)")			
			.endfor();
			
			set("window.lastPhrase", json.attr("text"));			
		};
		
		var(_jsSyllabe, "window.microlistener");
		var(jsonSyllable, jsvar(_jsSyllabe, ".aDataSet.getData()"));
		__("$.getJSON('"+JSSyllabisation.REST_JSON_SYLLABISATION+"').done(", fct(json).__(onJson) ,")");
				
		return _void();	
	}
	
}
