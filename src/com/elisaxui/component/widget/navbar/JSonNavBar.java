/**
 * 
 */
package com.elisaxui.component.widget.navbar;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JSonNavBar extends JSClass {

	interface JSonNavBarRow extends JSClass
	{
		public JSString type();
	}
	
	interface JSonNavBarBurger extends JSonNavBarRow
	{
	}
	
	interface JSonNavBarTitle extends JSonNavBarRow
	{
		public JSString title();
	}
	
	interface JSonNavBarBtnAction extends JSonNavBarRow
	{
		public JSString icon();
		public JSString idAction();
	}
	
	interface JSonNavBarBackground extends JSonNavBarRow
	{
		public JSString mode();
		public JSString css();
		public JSString opacity();
	}
	
}
