/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import static com.elisaxui.component.toolkit.JQuery.$;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.core.data.JSONBuilder;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSAnonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;

/**
 * @author gauth
 *
 *	DTOxxx : class json  !=   class JS
 *	JSxxx  : class JS
 */
public interface JSPerfVuesJS extends JSClass, JSONBuilder {

	public static boolean isVueJS() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("vue", "false").equals("true");
	}

	public static boolean isJQuery() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("query", "false").equals("true");
	}

	public static boolean isChange() {
		return XUIFactoryXHtml.getXHTMLFile().getFirstQueryParam("change", "true").equals("true");
	}

	default void doPerf() {
		JSInt i = JSClass.declareType(JSInt.class, "i");

		if (isVueJS()) {

			JSArray<Object> list = new JSArray<>().asLitteral();

			// creer un json en java
			JSAnonym doJava = () -> {
				DtoUser user = newInst(DtoUser.class);
				user.firstName().set("'toto'");
				list.push(user);

				list.push("test");
				list.push(1);
				list.push(1.2);

				JSArray<Object> list2 = new JSArray<>();
				list2.push("A");
				list2.push("B");

				list.push(list2);

				user = newInst(DtoUser.class);
				user.firstName().set("'tata'");
				list.push(user);
			};

			callJava(doJava);

			JSArray<?> json = let(JSArray.class, "json", list.getStringJSON());
			
			JSArray<?> json2 = let(JSArray.class, "json2",
					arr(obj(    	// version literral   [{"a":1,"b":"aaa","c":{"a":1,"b":2},"d":[1,2,3]}]
							v("a", 1),
							v("b", "aaa"),
							v("c", obj(v("a", 1), v("b", 2))),
							v("d", arr(1, 2, 3)))));
			JSArray<?> json3 = let(JSArray.class, "json3", arr("a", "b", 1));   //['a','b',1]

			DtoTest d = let("d", newInst(DtoTest.class).asLitteral()); // asLitteral: ne create pas le new DtoTest()

			d.users().set(newInst(JSArray.class)); // d.users=new Array()
			d.users().set(new JSArray<>().asLitteral()); // d.users=[]

			JSObject a = let("a", newInst(JSObject.class)); // let a=new Object()
			JSObject b = let("b", newInst(JSObject.class).asLitteral());   // 

			a.set(json);
			b.set(json2);
			b.set(json3);
			a.set(b);

			__("new Vue({ el: '#app', data: ", d, " })");   //TODO a mettre un XID

			_forIdxBetween(i, 0, 20000)._do(() -> {
				DtoUser user = newInst(DtoUser.class).asLitteral(); // pas de let et pas de declaration de class
				user.firstName().set(txt("vuejs ", i));
				d.users().push(user);
			});

			if (isChange()) {
				setTimeout(() -> _forIdxBetween(i, 0, 20000)._do(
						() -> {
							d.users().at(i).firstName().set(txt("vuejs ", calc("20000-", i)));
							setTimeout(
									() -> _forIdxBetween(i, 0, 20000)._do(() -> d.users().pop()),
									2000);
						}),
						100);
			}

		} else if (isJQuery()) {
			JQuery jqUsers = let("jqUsers", $("#users"));
			
			JSString d = let("d", JSString.value(""));    // new JSString()
			_forIdxBetween(i, 0, 20000)._do(  
					() -> d.append(txt("<li id='q", i, "'>que", i, "</li>")));

			jqUsers.append(d);

			if (isChange()) {
				JQuery jqchildren = let(JQuery.class, "jqchildren", jqUsers.children());
				setTimeout(
						() -> _forIdxBetween(i, 0, 20000)._do(
								() -> jqchildren.eq(i).text(txt("que", calc("20000-", i)))),
						100);
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
				setTimeout(
						() -> _forIdxBetween(i, 3, 20000)._do(
								() -> __(children.at(i), ".innerText='dom'+(20000-i)")),
						100);
			}
		}

	}
}
