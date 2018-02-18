/**
 * 
 */
package com.elisaxui.component.toolkit;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

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
		return _callMth("addClass", addParamMth(classes));
	}
	
	public JQuery removeClass(Object...classes)
	{
		return _callMth("removeClass", addParamMth(classes));
	}
	
	public JQuery detach()
	{
		return _callMth("detach", null);
	}
	
	public JQuery get(Object value)
	{
		return _callMth("get", value);
	}
	
	public JQuery eq(Object value)
	{
		return _callMth("eq", value);
	}
	
	public JQuery text(Object... value)
	{
		return _callMth("text", addParamMth(value));
	}
	
	public JSInt length()
	{
		return  new JSInt()._setValue(attr("length"));
	}
	
	public JQuery val(Object... value)
	{
		return _callMth("val", value);
	}
	
	public JQuery prepend(Object...html)
	{
		return _callMth("prepend", html);
	}
	
	public JQuery append(Object...html)
	{
		return _callMth("append", html);
	}
	
	/**
	 * @return
	 */
	public JQuery remove(Object...selector) {
		return _callMth("remove", addParamMth(CSSSelector.onPath(selector)));
	}
	
	public JQuery css(Object attr, Object value)
	{
		return _callMth("css", addParamMth(attr), "," ,addParamMth(value));
	}
	
	public JQuery on(Object...classes)
	{
		return _callMth("on", classes);
	}

	public JQuery each(Object...classes)
	{
		return _callMth("each", classes);
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public Object closest(Object...selector) {
		return _callMth("closest", addParamMth(CSSSelector.onPath(selector)));
	}

	/**
	 * @param classes
	 * @return
	 */
	public Object hasClass(Object...classes) {
		return _callMth("hasClass", addParamMth(classes));
	}

	/**
	 * @param selector
	 * @return
	 */
	public JQuery children(Object...selector) {
		return _callMth("children", addParamMth(CSSSelector.onPath(selector)));
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public JQuery find(Object...selector) {
		Object p = (selector.length==1 & selector[0] instanceof JSAny)?  selector[0] : addParamMth(CSSSelector.onPath(selector));
		
		return _callMth("find", p);
	}
	
	public Object hide(Object time, Object fct) {
		return _callMth("hide", time, "," , fct);
	}
	
	public Object scrollTop(Object...param) {
		return _callMth("scrollTop", param);
	}
	
	
	public Object data(Object attr, Object value) {
		return _callMth("data", addParamMth(attr), "," ,addParamMth(value));
	}
	
	public Object data(Object attr) {
		return _callMth("data", addParamMth(attr));
	}
	
	public Object height(Object... attr) {
		return _callMth("height", addParamMth(attr));
	}
	/*
	 * Padding + Border + (margin optionel)
	 */
	public Object outerHeight(Object...withMargin) {
		return _callMth("outerHeight", withMargin);
	}


}
