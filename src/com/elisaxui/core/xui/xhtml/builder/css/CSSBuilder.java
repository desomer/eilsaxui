/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

/**
 * @author gauth
 *
 */
public class CSSBuilder {

	Object current;
	
	protected CSSDomElement on(CSSDomElement selector, CssBlock css)
	{
		css.run();
		if (selector.getListStyle().getLast().content==null)
			selector.getListStyle().getLast().content="";
		return selector.set(current.toString());
	}
	
	protected CSSDomElement sel(Object... selector)
	{
		CSSDomElement dom = new CSSDomElement();
		 
		return dom.path(selector);
	}
	
	protected void css(Object... selector)
	{
		current = selector[0];
	}
	
	protected CSSDomElement media(Object... selector)
	{
		return null;
	}

	protected CSSDomElement keyFrame(Object selector)
	{
		return null;
	}
	
	
	protected void from(CssBlock css)
	{

	}
	
	protected void to(CssBlock css)
	{

	}
	
	protected void prct(Object value, CssBlock css)
	{

	}
	
	public interface CssBlock extends Runnable {

	}
	
}
