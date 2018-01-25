/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import static com.elisaxui.component.toolkit.JQuery.*;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;

/**
 * @author gauth
 *
 */
public interface JSPerfVuesJS extends JSClass {

	public static boolean isVueJS() {
		return XUIFactoryXHtml.getXHTMLFile().getFisrtParam("vues", "false").equals("true");
	}

	public static boolean isJQuery() {
		return XUIFactoryXHtml.getXHTMLFile().getFisrtParam("query", "false").equals("true");
	}

	public static boolean isChange() {
		return XUIFactoryXHtml.getXHTMLFile().getFisrtParam("change", "true").equals("true");
	}

	default Object doPerf() {
		JSInt i = JSClass.declareType(JSInt.class, "i");

		if (isVueJS()) {
			JSon d = let(JSon.class, "d", "{ users: [{ first_name: 'toto'}]}");

			__(" new Vue({ el: '#app', data: ", d, " })");

			_for("var i=0; i<20000; i++")._do(() -> {
				__(((JSArray) d.castAttr(new JSArray(), "users")).push(var("{ first_name: 'vue'+i}")));
			});

			if (isChange())
				setTimeout(fct().__(() -> {
					JSArray<JSon> users = let(JSArray.class, "users", d.get("users"));
					users._type = JSon.class;
					_for("var i=0; i<20000; i++")
							._do(() -> users.at("i").get("first_name").set(calc(txt("vues"), "+(20000-i)")));
				}), 100);

		} else if (isJQuery()) {
			JQuery jqUsers = let(JQuery.class, "jqUsers", $("#users"));

			JSString d = let("d", JSString.value(""));
			_forIdxBetween(i, 0, 20000)._do(() -> {
				d.append(txt("<li>que", i, "</li>"));
			});
			jqUsers.append(d);

			if (isChange()) {
				JQuery jqchildren = let(JQuery.class, "jqchildren", jqUsers.children());
				setTimeout(fct().__(() -> { _forIdxBetween(i, 0, 20000)
						._do(() -> {
							jqchildren.eq(i).text(txt("que", var("(20000-i)")));
						});}), 100);
			}

		} else {
			JSVariable users = let(JSVariable.class, "users", "document.getElementById('users')");
			_for("var i=0; i<20000; i++")._do(() -> {
				_var("node", "document.createElement('li')");
				_var("textnode", "document.createTextNode('dom'+i)");
				__("node.appendChild(textnode);");
				__("users.appendChild(node);");
			});
			if (isChange()) {
				JSVariable children = let(JSVariable.class, "children", "document.getElementById('users').childNodes");
				setTimeout(fct().__(() -> {
					_for("var i=3; i<20000; i++");
					__("children[i].innerText='dom'+(20000-i)");
					endfor();
				}), 100);
			}
		}

		return "null";
	}

	/*
	 * // JQuery $li = let(JQuery.class, "$li", JQuery.$("<li>que</li>")); //
	 * _for("var i=0; i<20000; i++")._do(()->{ // __($users.append(calc($li,
	 * ".clone().text(", txt("que", var("i")),")"))); //
	 * //__($users.append(txt("<li>que", var("i") , "</li>"))); // });
	 */
}
