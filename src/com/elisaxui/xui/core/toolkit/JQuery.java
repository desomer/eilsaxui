/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;
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
		if (selector.length==1 && selector[0] instanceof JSVariable)
			return $((JSVariable)selector[0]);	
		else
			return $(CSSSelector.onPath(selector));
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
	
	public JQuery get(Object value)
	{
		return callMth("get", value);
	}
	
	public Object length()
	{
		return attr("length");
	}
	
	public JQuery val(Object... value)
	{
		return callMth("val", value);
	}
	
	public JQuery prepend(Object...html)
	{
		return callMth("prepend", html);
	}
	
	public JQuery append(Object...html)
	{
		return callMth("append", html);
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
	
	/**
	 * @param selector
	 * @return
	 */
	public Object find(Object...selector) {
		return callMth("find", addText(CSSSelector.onPath(selector)));
	}
	
	public Object hide(Object time, Object fct) {
		return callMth("hide", time, "," , fct);
	}
	
	public Object scrollTop(Object...param) {
		return callMth("scrollTop", param);
	}
	
	
	public Object data(Object attr, Object value) {
		return callMth("data", addText(attr), "," ,addText(value));
	}
	
	public Object data(Object attr) {
		return callMth("data", addText(attr));
	}
	/*
	 * Padding + Border + (margin optionel)
	 */
	public Object outerHeight(Object...withMargin) {
		return callMth("outerHeight", withMargin);
	}
}
