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
import com.elisaxui.core.xui.xml.builder.IResourceLoader;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;

/**
 * @author Bureau
 *   https://github.com/krasimir/queue
 *   
 */
@xComment("TKQueue")
public class TKQueue extends XHTMLPart implements IResourceLoader {
			
	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)	
	public XMLElement xAddJS() {	
		return xScriptJS( js()
				.__(loadResource("TKQueue.js"))
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
