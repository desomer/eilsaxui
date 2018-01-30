/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import static com.elisaxui.component.toolkit.JQuery.$;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import javax.json.*;

/**
 * @author gauth
 *
 */
public interface JSPerfVuesJS extends JSClass {

	public static boolean isVueJS() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("vue", "false").equals("true");
	}

	public static boolean isJQuery() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("query", "false").equals("true");
	}

	public static boolean isChange() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("change", "true").equals("true");
	}

	default Object doPerf() {
		JSInt i = JSClass.declareType(JSInt.class, "i");

		if (isVueJS()) {

			modeJava(() -> {
				JSArray<Object> list = new JSArray<>();
				DtoUser user = newInst(DtoUser.class);
				user.firstName().set("toto");
				list.push(user);
				
				list.push("test");
				list.push(1);
				list.push(1.2);
				
				JSArray<Object> list2 = new JSArray<>();
				list2.push("A");
				list2.push("B");
				list.push(list2);

				user = newInst(DtoUser.class);
				user.firstName().set("tata");
				list.push(user);

				System.out.println("list java = " + list._getValue());
			});

			DtoTest d = let("d", newInst(DtoTest.class).asLitteral());
			d.users().set(newInst(JSArray.class));
			d.users().set(new JSArray<>().asLitteral());

			JSObject a = let("a", newInst(JSObject.class));
			JSObject b = let("b", newInst(JSObject.class).asLitteral());
			
			a.set(b);

			__("new Vue({ el: '#app', data: ", d, " })");

			_forIdxBetween(i, 0, 20000)._do(() -> {
				DtoUser user = newInst(DtoUser.class).asLitteral(); // pas de let et pas de declaration de class
				user.firstName().set(txt("vue", i));
				d.users().push(user);
			});

			if (isChange()) {
				setTimeout(() -> _forIdxBetween(i, 0, 20000)
						._do(() -> {
							d.users().at(i).firstName().set(txt("vues", calc("20000-", i)));
							setTimeout(() -> _forIdxBetween(i, 0, 20000)
									._do(() -> d.users().pop()),
									2000);
						}),
						100);
			}

		} else if (isJQuery()) {
			JQuery jqUsers = let(JQuery.class, "jqUsers", $("#users"));

			JSString d = let("d", JSString.value(""));
			_forIdxBetween(i, 0, 20000)._do(() -> d.append(txt("<li id='q", i, "'>que", i, "</li>")));
			jqUsers.append(d);

			if (isChange()) {
				JQuery jqchildren = let(JQuery.class, "jqchildren", jqUsers.children());
				setTimeout(() -> _forIdxBetween(i, 0, 20000)
						._do(() -> jqchildren.eq(i).text(txt("que", calc("20000-",i)))), 100);
			}

		} else {
			JSVariable users = let(JSVariable.class, "users", "document.getElementById('users')");

			_forIdxBetween(i, 0, 20000)._do(() -> {
				_var("node", "document.createElement('li')");
				_var("textnode", "document.createTextNode('dom'+i)");
				__("node.appendChild(textnode);");
				__(users, ".appendChild(node);");
			});

			if (isChange()) {
				JSArray<?> children = let(JSArray.class, "children", "document.getElementById('users').childNodes");
				setTimeout(	() -> _forIdxBetween(i, 3, 20000)._do(() -> __(children.at(i), ".innerText='dom'+(20000-i)")),
						100);
			}
		}

		return "null";
	}
}
