/**
 * 
 */
package com.elisaxui.component.widget.navbar.old;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
@Deprecated
public interface JSonNavBar extends JSClass {

	@Deprecated
	interface JSonNavBarRow extends JSClass
	{
		public JSString type();
	}
	@Deprecated
	interface JSonNavBarBurger extends JSonNavBarRow
	{
	}
	@Deprecated
	interface JSonNavBarTitle extends JSonNavBarRow
	{
		public JSString title();
	}
	@Deprecated
	interface JSonNavBarBtnAction extends JSonNavBarRow
	{
		public JSString icon();
		public JSString idAction();
	}
	@Deprecated
	interface JSonNavBarBackground extends JSonNavBarRow
	{
		public JSString mode();
		public JSString css();
		public JSString opacity();
	}
	
}
