/**
 * 
 */
package com.elisaxui.xui.elisys.app;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.xui.elisys.widget.JSSyllabisation;

/**
 * @author Bureau
 *
 */
public interface JSHistoireManager extends JSClass {
	
	JSSyllabisation _jsSyllabe = null;
	JSVariable _jsonSyllable = null;
	
	default Object getHistoire()
	{

		var(_jsSyllabe, "window.microlistener");
		var(_jsonSyllable, jsvar(_jsSyllabe, ".aDataSet.getData()"));
		__("$.getJSON('"+JSSyllabisation.REST_JSON_SYLLABISATION+"').done(", fct("a")
				.var("lesmots", "a.mots")
				
				._for("var i = 0, l = lesmots.length; i < l; i++")
					.__("setTimeout(", fct("num")
							.__(_jsonSyllable,".push(lesmots[num])")
						, ",50+(20*i), i)")			
					
				.endfor()
				
				.set("window.lastPhrase", "a.text")
			,")");
				
		return _void();	
	}
	
}
