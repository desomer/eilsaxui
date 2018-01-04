/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.custom;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JSClass1 extends JSClass {

	static final JSString OK = JSString.value("ok");    // constante
	
	JSString valeur();
	JSArray<JSString> tableau();
	
	default void constructor()
	{
		valeur().set("constructor");
		tableau().set("[]");
	}
	
	default JSString doSomething()
	{
		JSString a = let("a", JSString.value("test"));
		doSomething2(a);
		doSomething2(JSString.value("12"));
		a.set("12");
		tableau().push(a);              // les tableau
		a.set(tableau().at(0).add(5));
		
		consoleDebug(txt("ok"), a);
		
		return a;
	}
	
	default void doSomething2(JSString a)
	{
		JSString p = let("p",  OK);
		JSString b = (JSString) let("b", a.add("5"));
		b.set(b.add(a));
		b.set(p.add("5").add(12)); // chainage
		p.set(calc(b, "+", 5, "+", OK));     //calcule complexe
	}
}
