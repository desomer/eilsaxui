/**
 * 
 */
package com.elisaxui.component.widget.activity;

import com.elisaxui.component.widget.container.JSViewCard;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;

/**
 * @author Bureau
 *
 */
@Deprecated
public abstract class JSONActivity implements IJSONBuilder
{
	
	/**
	 * 
	 */
	private static final String MODE = "mode";
	private static final String ATTR_ID_ACTION = "idAction";
	private static final String ATTR_ICON = "icon";
	private static final String ATTR_HTML = "html";
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_ACTION = "action";
	
	
	public static final String FACTORY_NAVBAR = "JSNavBar";
	public static final String FACTORY_CONTAINER = "JSContainer";
	
	/**************************************************************************************/
	public Object activity(String id, Object children, Object events)
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

	/************************************* EVENT ****************************************/
	public Object routeTo(String url)
	{
		return  obj( v(ATTR_ACTION, "route"), v("url", url));
	}
	
	
	public Object callbackTo(Object fct, Object param)
	{
		return  obj( v(ATTR_ACTION, "callback"), v("fct", fct), v("param", param));
	}
	
	public Object goBack()
	{
		return  obj( v(ATTR_ACTION, "back"));
	}
	
	/******************************** FLOAT ACTION  ************************************/
	
	public Object floatAction(String icon, String idAction)
	{
		return  obj( v(ATTR_TYPE, "floatAction"), v(ATTR_ICON, icon), v(ATTR_ID_ACTION, idAction));
	}
	
	/******************************** HEADER ******************************************/
		
	public Object btnBurger()
	{
		return  obj( v(ATTR_TYPE, "burger"));
	}
	
	public Object btnActionIcon(String icon, String idAction)
	{
		return  obj( v(ATTR_TYPE, ATTR_ACTION), v(ATTR_ICON, icon), v(ATTR_ID_ACTION, idAction));
	}
		
	public Object title(String title)
	{
		return  obj( v(ATTR_TYPE, "title"), v("title", title));
	}
	
	/*********************************** CARD ***********************************/
	public Object cardHtml(Object html)
	{
		return  obj( v(ATTR_TYPE,"card"), v(ATTR_HTML,html));
	}
	
	public Object card(Object rows)
	{
		return  obj( v(ATTR_TYPE,"card"), v("rows", rows));
	}
	
	public Object text(String text)
	{
		return  obj( v(ATTR_TYPE, JSViewCard.TYPE_TEXT), v(ATTR_HTML, text));
	}
	
	public Object cardAction(String idAction)
	{
		return  obj( v(ATTR_TYPE, JSViewCard.TYPE_CARD_ACTION),v(ATTR_ID_ACTION, idAction));
	}
	
	public Object backgroundGradiant()
	{
		return  obj( v(ATTR_TYPE, JSViewCard.TYPE_BACKGROUND), v(MODE, "granim"));
	}
	
	public Object backgroundImage(String url, double opacity)
	{
		return  obj( v(ATTR_TYPE, JSViewCard.TYPE_BACKGROUND), v(MODE, "css"), v("css", "url(" +url +") center / cover no-repeat"), v("opacity", opacity));
	}
	
	
}
