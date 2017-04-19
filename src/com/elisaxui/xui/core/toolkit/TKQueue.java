/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import java.util.ArrayList;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

/**
 * @author Bureau
 *   https://github.com/krasimir/queue
 *   
 */
@xComment("TKQueue")
public class TKQueue extends XHTMLPart {

	String queuejs = "var TKQueue = function() {\n"+
			"\tvar api = null, self = this;\n"+
			"\tvar\tqueueElements = [], queueElementsSource = [];\n"+
			"\tvar\tisNumber = function(n) { return !isNaN(parseFloat(n)) && isFinite(n); };\n"+
			"\tvar flags = {}, interval = null;\n"+
			"\tvar processing = function() {\n"+
			"\t\tif(queueElements.length > 0) {\n"+
			"\t\t\tvar item = queueElements.shift();\n"+
			"\t\t\tif(flags.stop !== true) {\n"+
			"\t\t\t\tif(isNumber(item)) { // delay\n"+
			"\t\t\t\t\tinterval = setTimeout(api, item);\n"+
			"\t\t\t\t} else if(typeof item === \'function\') { // functions\n"+
			"\t\t\t\t\titem();\n"+
			"\t\t\t\t\tapi();\n"+
			"\t\t\t\t}\n"+
			"\t\t\t} else {\n"+
			"\t\t\t\tclearTimeout(interval);\n"+
			"\t\t\t}\n"+
			"\t\t} else {\n"+
			"\t\t\tif(typeof flags.callback !== \'undefined\') flags.callback();\n"+
			"\t\t\tif(flags.loop) {\n"+
			"\t\t\t\tqueueElements = [];\n"+
			"\t\t\t\tfor(var i=0; el=queueElementsSource[i]; i++) {\n"+
			"\t\t\t\t\tqueueElements.push(el);\n"+
			"\t\t\t\t}\n"+
			"\t\t\t\tapi();\n"+
			"\t\t\t}\n"+
			"\t\t}\t\n"+
			"\t}\n"+
			"\tvar filling = function() {\n"+
			"\t\tvar item = arguments[0];\n"+
			"\t\tif(isNumber(item) || typeof item === \'function\') {\n"+
			"\t\t\tqueueElements.push(item);\n"+
			"\t\t\tqueueElementsSource.push(item);\t\n"+
			"\t\t} else if(typeof item === \'string\') {\n"+
			"\t\t\tflags[item] = arguments[1] || true;\n"+
			"\t\t}\n"+
			"\t}\n"+
			"\treturn api = function() {\n"+
			"\t\targuments.length === 0 ? processing() : filling.apply(self, arguments);\n"+
			"\t\treturn api;\n"+
			"\t}\n"+
			"\treturn api;\n"+
			"}";
			
	@xTarget(HEADER.class)
	@xRessource
	public Element xAddJS() {
		return xScriptJS(
				js().__(queuejs));
	}
	
	public static final Object[] start(Object... param)
	{
		ArrayList<Object> ret = new ArrayList<>();
		ret.add("window.animInProgess=true;\nTKQueue()");
		for (Object  object: param) {
			ret.add("(");
			ret.add(object);
			ret.add(")");
		}
		ret.add("(\"callback\", function() {window.animInProgess=false})");
		ret.add("()");
		return ret.toArray();
	}
	
}
