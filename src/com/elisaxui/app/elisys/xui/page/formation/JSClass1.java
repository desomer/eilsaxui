/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *   
 *    https://fr.quora.com/Quel-est-le-meilleur-framework-JavaScript-du-moment-parmi-Angular-React-ou-Vue
 *
 *	    gestion    ? N=-1 & mode=IE5 & fork=B   & display = {diff}
 *							
 *
 */
public interface JSClass1 extends JSClass {

	static final JSString OK = JSString.value("ok"); // constante
	JSInt idxc = JSContent.declareType(JSInt.class, "idxc"); // declare type sans let

	JSString valeur();

	JSArray<JSString> tableau();

	default void constructor(JSString txt) { // mot reserver
		valeur().set("constructor"); // le string est une string
		tableau().set("[]"); // le string est un tableau
	}

	default JSString doSomething() {
		JSString a = let("a", JSString.value("test"));
		doSomething2(a);
		doSomething2(JSString.value("1"));
		a.set("13");

		// les tableaux
		JSArray<JSClass1> listObj = let("listObj", cast(JSArray.class, "[]"));
		JSArray<JSClass1> listObj2 = let(JSArray.class, "listObj2", "[]");
		JSArray<JSString> listObj3 = let("listObj3", cast(JSArray.class, "[]"));

		listObj.push(newJS(JSClass1.class));
		listObj2.push(newJS(JSClass1.class, a));   // a param de constructor
		listObj3.push(JSString.value("12"));

		JSClass1 obj = let(JSClass1.class, "obj", listObj.at(0));
		_if(false)._then(() -> {
			obj.doSomething();
		});

		listObj2.at(0).doSomething2(a);
		listObj3.at(0).substring(1);

		tableau().push(a); 
		a.set(tableau().at(0).add(5));

		consoleDebug(txt("ok"), a);   // console debug

		// les condition
		_if(a, ">", 13)._then(() -> {
			a.set(13);
		})._elseif(a, "<", 13)._then(() -> {
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
		let(bool, test.equalsJS(555));
		test.set(569);

		JSString p = let("p", OK); // let & declare type
		JSString b = (JSString) let("b", a.add("5"));

		b.set(b.add(a)); // ajout b = b + a
		b.set(p.add("5").add(12)); // chainage b = p+"5"+12
		p.substring(1).substring(0, 2);
		p.set(calc(b, "+", 5, "+", OK)); // calcule complexe js

		_forIdx("idxx", tableau())._do( () -> { 
			b.set(tableau().at("idxx")); 
		});

		// boucle for
		_forIdx(idxc, tableau())._do( () -> {
			b.set(tableau().at(idxc));
		});
		
		// selector en text
		p.set(txt(CSSSelector.onPath(XUIScene.scene.and(XUIScene.cShell)
											.directChildren(ViewOverlay.cBlackOverlay))
				));
		
		p.set(txt(CSSSelector.onPath(ViewOverlay.cBlackOverlay.or( 
										XUIScene.scene.descendant(XUIScene.cShell)))
				));

		// new instance
		JSClass1 aClass = let("overlay", newJS(JSClass1.class)); // new class
		_if(false)._then(aClass::doSomething);

	}
}
