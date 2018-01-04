/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.custom;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JSClass1 extends JSClass {

	default JSString doSomething()
	{
		JSString a = let(JSString.class, "a", txt("test"));
	//	JSString a = let(@name("a")JSString.value("test"));
		doSomething2(a);
		a.set("12");
		return a;
	}
	
	default void doSomething2(JSString a)
	{
		JSString b = let(JSString.class, "b", a);
		b.set(b.add(a));
		b.set(12); 
	}
}
