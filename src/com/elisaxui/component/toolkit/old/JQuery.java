/**
 * 
 */
package com.elisaxui.component.toolkit.old;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;

/**
 * @author Bureau
 *
 */
public class JQuery extends JSAny  implements IJSClassInterface {

	public static final JQuery $(CSSSelector selector)
	{
		JQuery ret = new JQuery();
		ret.addContent("$(\"");
		ret.addContent(selector);
		ret.addContent("\")");
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
		
	public JQuery addClass(Object...classes)
	{
		return callMth("addClass", classes);
	}
	
	public JQuery removeClass(Object...classes)
	{
		return callMth("removeClass", classes);
	}
	
	public JQuery detach()
	{
		return callMth("detach");
	}
	
	public JQuery get(Object value)
	{
		return callMth("get", value);
	}
	
	public JQuery eq(Object value)
	{
		return callMth("eq", value);
	}
	
	public JQuery text(Object... value)
	{
		return callMth("text", value);
	}
	
	public JSInt length()
	{
		return  new JSInt()._setValue(attr("length"));
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
	
	/**
	 * @return
	 */
	public JQuery remove(Object...selector) {
		return callMth("remove", CSSSelector.onPath(selector));
	}
	
	public JQuery css(Object attr, Object value)
	{
		return callMth("css", attr ,value);
	}
	
	public JQuery on(Object...classes)
	{
		return callMth("on", classes);
	}

	public JQuery each(Object...classes)
	{
		return callMth("each", classes);
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public Object closest(Object...selector) {
		return callMth("closest", CSSSelector.onPath(selector));
	}

	/**
	 * @param classes
	 * @return
	 */
	public Object hasClass(Object...classes) {
		return callMth("hasClass", classes);
	}

	/**
	 * @param selector
	 * @return
	 */
	public JQuery children(Object...selector) {
		return callMth("children", CSSSelector.onPath(selector));
	}
	
	/**
	 * @param selector
	 * @return
	 */
	public JQuery find(Object...selector) {
		Object p = (selector.length==1 & selector[0] instanceof JSAny)?  selector[0] : addParamMth(CSSSelector.onPath(selector));
		
		return callMth("find", p);
	}
	
	public Object hide(Object time, Object fct) {
		return callMth("hide", time,fct);
	}
	
	public Object scrollTop(Object...param) {
		return callMth("scrollTop", param);
	}
	
	
	public Object data(Object attr, Object value) {
		return callMth("data", attr, value);
	}
	
	public Object data(Object attr) {
		return callMth("data", attr);
	}
	
	public Object height(Object... attr) {
		return callMth("height", attr);
	}
	/*
	 * Padding + Border + (margin optionel)
	 */
	public Object outerHeight(Object...withMargin) {
		return callMth("outerHeight", withMargin);
	}


}
