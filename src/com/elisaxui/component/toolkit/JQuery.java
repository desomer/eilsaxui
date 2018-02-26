/**
 * 
 */
package com.elisaxui.component.toolkit;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSClassInterface;

/**
 * @author Bureau
 *
 */
public class JQuery extends JSClassInterface {

	public static final JQuery $(CSSSelector selector)
	{
		JQuery ret = new JQuery();
		ret.addContent("$('");
		ret.addContent(selector);
		ret.addContent("')");
		return ret;
	}
	
	public static final JQuery $(JSAny variable)
	{
		JQuery ret = new JQuery();
		ret.addContent("$(");
		ret.addContent(variable);
		ret.addContent(")");
		return ret;
	}
	
	public static final JQuery $(Object... selector)
	{
		if (selector.length==1 && selector[0] instanceof JSAny)
			return $((JSAny)selector[0]);	
		else
			return $(CSSSelector.onPath(selector));
	}
		
	/**TODO remplacer par une annotation */
	public JQuery addClass(Object...classes)
	{
		return call("addClass", addParamMth(classes));
	}
	
	public JQuery removeClass(Object...classes)
	{
		return call("removeClass", addParamMth(classes));
	}
	
	public JQuery detach()
	{
		return call("detach", null);
	}
	
	public JQuery get(Object value)
	{
		return call("get", value);
	}
	
	public JQuery eq(Object value)
	{
		return call("eq", value);
	}
	
	public JQuery text(Object... value)
	{
		return call("text", addParamMth(value));
	}
	
	public JSInt length()
	{
		return  new JSInt()._setValue(attr("length"));
	}
	
	public JQuery val(Object... value)
	{
		return call("val", value);
	}
	
	public JQuery prepend(Object...html)
	{
		return call("prepend", html);
	}
	
	public JQuery append(Object...html)
	{
		return call("append", html);
	}
	
	/**
	 * @return
	 */
	public JQuery remove(Object...selector) {
		return call("remove", addParamMth(CSSSelector.onPath(selector)));
	}
	
	public JQuery css(Object attr, Object value)
	{
		return call("css", addParamMth(attr), JSAny.SEP ,addParamMth(value));
	}
	
	public JQuery on(Object...classes)
	{
		return call("on", classes);
	}

	public JQuery each(Object...classes)
	{
		return call("each", classes);
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public Object closest(Object...selector) {
		return call("closest", addParamMth(CSSSelector.onPath(selector)));
	}

	/**
	 * @param classes
	 * @return
	 */
	public Object hasClass(Object...classes) {
		return call("hasClass", addParamMth(classes));
	}

	/**
	 * @param selector
	 * @return
	 */
	public JQuery children(Object...selector) {
		return call("children", addParamMth(CSSSelector.onPath(selector)));
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public JQuery find(Object...selector) {
		Object p = (selector.length==1 & selector[0] instanceof JSAny)?  selector[0] : addParamMth(CSSSelector.onPath(selector));
		
		return call("find", p);
	}
	
	public Object hide(Object time, Object fct) {
		return call("hide", time, JSAny.SEP , fct);
	}
	
	public Object scrollTop(Object...param) {
		return call("scrollTop", param);
	}
	
	
	public Object data(Object attr, Object value) {
		return call("data", addParamMth(attr), JSAny.SEP ,addParamMth(value));
	}
	
	public Object data(Object attr) {
		return call("data", addParamMth(attr));
	}
	
	public Object height(Object... attr) {
		return call("height", addParamMth(attr));
	}
	/*
	 * Padding + Border + (margin optionel)
	 */
	public Object outerHeight(Object...withMargin) {
		return call("outerHeight", withMargin);
	}


}
