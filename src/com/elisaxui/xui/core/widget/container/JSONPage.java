/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.data.json.JSONBuilder;

/**
 * @author Bureau
 *
 */
public class JSONPage extends JSONBuilder
{
	
	/**
	 * 
	 */
	private static final String ATTR_HTML = "html";

	/**
	 * 
	 */
	private static final String ATTR_TYPE = "type";

	/**
	 * 
	 */
	private static final String ATTR_ACTION = "action";
	
	public static final String FACTORY_NAVBAR = "JSNavBar";
	public static final String FACTORY_CONTAINER = "JSContainer";
	
	/**************************************************************************************/
	public Object page(String id, Object children, Object events)
	{
		return  obj( v(ATTR_TYPE, "page"), 
					 v("id", id), 
					 v("children", children),
					 v("events", events)); 
	}
	
	public Object factory(String selector, String factory, Object rows )
	{
		
		return  obj( v("selector", selector), v("factory", factory), v("rows", rows));
	} 

	public Object routeTo(String url)
	{
		return  obj( v(ATTR_ACTION, "route"), v("url", url));
	}
	
	
	public Object callbackTo(Object fct)
	{
		return  obj( v(ATTR_ACTION, "callback"), v("fct", fct));
	}
	
	
	/**********************************************************************************/
	
	public Object floatAction()
	{
		return  obj( v(ATTR_TYPE, "floatAction"));
	}
	
	
	public Object cardHtml(Object html)
	{
		return  obj( v(ATTR_TYPE,"card"), v(ATTR_HTML,html));
	}
	
	public Object card(Object rows)
	{
		return  obj( v(ATTR_TYPE,"card"), v("rows", rows));
	}
	
	public Object btnBurger()
	{
		return  obj( v(ATTR_TYPE, "burger"));
	}
	
	public Object btnActionIcon(String icon, String idAction)
	{
		return  obj( v(ATTR_TYPE, ATTR_ACTION), v("icon", icon), v("idAction", idAction));
	}
		
	public Object title(String title)
	{
		return  obj( v(ATTR_TYPE, "name"), v("name", title));
	}
	
	public Object text(String text)
	{
		return  obj( v(ATTR_TYPE, "text"), v(ATTR_HTML, text));
	}
	
	public Object backgroundImage(String url, double opacity)
	{
		return  obj( v(ATTR_TYPE, "background"), v("mode", "css"), v("css", "url(" +url +") center / cover no-repeat"), v("opacity", opacity));
	}
	
	
}
