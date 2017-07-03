/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;

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
		return callMth("removeClass", classes);
	}
	
}
