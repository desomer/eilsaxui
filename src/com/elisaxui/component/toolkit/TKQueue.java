/**
 * 
 */
package com.elisaxui.component.toolkit;

import java.util.ArrayList;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent.JSNewLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xExtend;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.IResourceLoader;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;

/**
 * @author Bureau https://github.com/krasimir/queue
 * 
 * 
 *         TODO a supprimer : changer par des promises de fin d'animation
 */
@xComment("TKQueue")
public class TKQueue extends XHTMLPart {

	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xAddJS() {
		return xElem(JSQueue.class);
	}

	@xCoreVersion("1")
	@xTarget // TODO a faire marcher
	@xExtend(file = "TKQueue.js")
	public interface JSQueue extends JSClass, IResourceLoader {

		@xStatic(autoCall = true)
		default void init() {
			__(loadResource("TKQueue.js", true));
			__("window.animInProgess=false");
		}

	}

	public static final Object[] startAnimQueued(Object... param) {
		ArrayList<Object> ret = new ArrayList<>();
		ret.add("window.animInProgess=true;");
		ret.add(JSNewLine.class);
		ret.add("TKQueue()");
		for (Object object : param) {
			ret.add("(");
			ret.add(object);
			ret.add(")");
		}
		ret.add("(\"callback\", function() {window.animInProgess=false})");
		ret.add("()");
		return ret.toArray();
	}

	public static final Object[] startProcessQueued(Object... param) {
		ArrayList<Object> ret = new ArrayList<>();
		ret.add("TKQueue()");
		for (Object object : param) {
			ret.add("(");
			ret.add(object);
			ret.add(")");
		}
		ret.add("()");
		return ret.toArray();
	}

}
