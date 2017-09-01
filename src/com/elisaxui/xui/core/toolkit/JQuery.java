/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;

/**
 * @author Bureau
 *
 */
public class JQuery extends JSClassMethod {

	public static final JQuery $(CSSSelector selector)
	{
		JQuery ret = new JQuery();
		ret.addContent("$('");
		ret.addContent(selector);
		ret.addContent("')");
		return ret;
	}
	
	public static final JQuery $(JSVariable variable)
	{
		JQuery ret = new JQuery();
		ret.addContent("$(");
		ret.addContent(variable);
		ret.addContent(")");
		return ret;
	}
	
	public static final JQuery $(Object... selector)
	{
		return $(CSSSelector.onPath(selector));
	}
	
	public Object[] addText(Object...classes)
	{
		Object[] ret =  new Object[3];
		ret[0]="'";
		ret[1]=classes[0];
		ret[2]="'";
		return ret;
	}
	
	/**TODO remplacer par une annotation */
	public JQuery addClass(Object...classes)
	{
		return callMth("addClass", addText(classes));
	}
	
	public JQuery removeClass(Object...classes)
	{
		return callMth("removeClass", addText(classes));
	}
	
	public JQuery detach()
	{
		return callMth("detach", null);
	}
	
	public JQuery prepend(Object...html)
	{
		return callMth("prepend", html);
	}
	
	public JQuery css(Object attr, Object value)
	{
		return callMth("css", addText(attr), "," ,addText(value));
	}
	
	public JQuery on(Object...classes)
	{
		return callMth("on", classes);
	}

	/**
	 * @param selector
	 * @return
	 */
	public Object closest(Object...selector) {
		return callMth("closest", addText(CSSSelector.onPath(selector)));
	}

	/**
	 * @param classes
	 * @return
	 */
	public Object hasClass(Object...classes) {
		return callMth("hasClass", addText(classes));
	}

	/**
	 * @param selector
	 * @return
	 */
	public Object children(Object...selector) {
		return callMth("children", addText(CSSSelector.onPath(selector)));
	}
	
}
