/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.custom;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JSClass1 extends JSClass {

	static final JSString OK = JSString.value("ok"); // constante

	JSString valeur();
	JSArray<JSString> tableau();

	default void constructor() {  // mot reserver
		valeur().set("constructor"); // le string est une string
		tableau().set("[]"); // le string est un tableau
	}

	default JSString doSomething() {
		JSString a = let("a", JSString.value("test"));
		doSomething2(a);
		doSomething2(JSString.value("12"));
		a.set("12");
		tableau().push(a); // les tableau
		a.set(tableau().at(0).add(5));

		consoleDebug(txt("ok"), a);

		_if(a, ">", 13).then(() -> {
			a.set(13);
		})._elseif(a, "<", 13).then(() -> {
			a.set(null);
		})._else(() -> {
			a.set("14");
		});

		return a;
	}

	default void doSomething2(JSString a) {

		JSInt test = declareType(JSInt.class, "test"); // declare type
		JSBool bool = declareType(JSBool.class, "bool");
		
		let(test, 1);
		let(bool, test.isEqual(2));
		
		JSString p = let("p", OK);    // let &  declare type
		JSString b = (JSString) let("b", a.add("5"));

		b.set(b.add(a)); // ajout b = b + a
		b.set(p.add("5").add(12)); // chainage b = p+"5"+12
		p.substring(1).substring(0, 2);
		p.set(calc(b, "+", 5, "+", OK)); // calcule complexe js
		
		// boucle for
		JSInt idx = declareType(JSInt.class, "idx"); // declare type sans let
		_forIdx(idx, tableau())._do(() -> {
			b.set(tableau().at(idx));
		});

		txt(CSSSelector.onPath(XUIScene.scene));
		
	}
}
