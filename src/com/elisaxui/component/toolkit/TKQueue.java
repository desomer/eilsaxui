/**
 * 
 */
package com.elisaxui.component.toolkit;

import java.util.ArrayList;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent.JSNewLine;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;

/**
 * @author Bureau
 *   https://github.com/krasimir/queue
 *   
 */
@xComment("TKQueue")
public class TKQueue extends XHTMLPart {

	String queuejs = "window.TKQueue = function() {"+
			"var api = null, self = this;\n"+
			"var queueElements = [], queueElementsSource = [];\n"+
			"var isNumber = function(n) { return !isNaN(parseFloat(n)) && isFinite(n); };\n"+
			"var flags = {}, interval = null;\n"+
			
			"var processing = function() {\n"+
			"	if(queueElements.length > 0) {\n"+
			"		var item = queueElements.shift();\n"+
			"		if(flags.stop !== true) {\n"+
			"			if(isNumber(item)) { // delay\n"+
			"				interval = setTimeout(api, item);\n"+
			"			} else if(typeof item === \'function\') { // functions\n"+
			"				fastdom.mutate(item);\n"+
			"				api();\n"+
			"			} else if(typeof item === \'string\' && item=='nextFrame' ) {\n"+
			"				fastdom.mutate( function() { fastdom.measure(api); });\n"+
			"			}\n"+
			"		} else {\n"+
						// en stop
			"			clearTimeout(interval);\n"+
			"		}\n"+
			"	} else {\n"+
			//        c'est la fin de la queue
			"		if(typeof flags.callback !== \'undefined\') flags.callback();\n"+
			"		if(flags.loop) {\n"+
			"				queueElements = [];\n"+
			"				for(var i=0; el=queueElementsSource[i]; i++) {\n"+
			"					queueElements.push(el);\n"+
			"				}\n"+
			"			api();\n"+
			"		}\n"+
			"	}\t\n"+
			"}\n"+
			
			"var filling = function() {\n"+
			"	var item = arguments[0];\n"+
			"	if(isNumber(item) || typeof item === \'function\') {\n"+
			"		queueElements.push(item);\n"+
			"		queueElementsSource.push(item);\t\n"+
			"	} else if(typeof item === \'string\') {\n"+
			"         if (item=='nextFrame')"+	
			"			{"+
			"				queueElements.push(item);\n"+
			"				queueElementsSource.push(item);\t\n"+
			"			}"+
			"         else"+
			"				flags[item] = arguments[1] || true;\n"+
			"	}\n"+
			"}\n"+
			
			"return api = function() {\n"+
			"		arguments.length === 0 ? processing() : filling.apply(self, arguments);\n"+
			"		return api;\n"+
			"	}\n"+
			
			"return api;\n"+
			"}";
			
	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)	
	public XMLElement xAddJS() {
		return xScriptJS( js().__(queuejs)
				.__("window.animInProgess=false")    // init a false
				);
	}
	
	/**
	 * indique 
	 * @param param
	 * @return
	 */
	public static final Object[] startAnimQueued(Object... param)
	{
		ArrayList<Object> ret = new ArrayList<>();
		ret.add("window.animInProgess=true;");
		ret.add(JSNewLine.class);
		ret.add("TKQueue()");
		for (Object  object: param) {
			ret.add("(");
			ret.add(object);
			ret.add(")");
		}
		ret.add("(\"callback\", function() {window.animInProgess=false})");
		ret.add("()");
		return ret.toArray();
	}
	
	public static final Object[] startProcessQueued(Object... param)
	{
		ArrayList<Object> ret = new ArrayList<>();
		ret.add("TKQueue()");
		for (Object  object: param) {
			ret.add("(");
			ret.add(object);
			ret.add(")");
		}
		ret.add("()");
		return ret.toArray();
	}
	
}
