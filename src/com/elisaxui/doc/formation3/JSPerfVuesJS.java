/**
 * 
 */
package com.elisaxui.doc.formation3;

import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.old.JQuery;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;

import static com.elisaxui.component.toolkit.old.JQuery.$;
import static com.elisaxui.core.xui.xhtml.builder.javascript.template.JSNodeTemplate.*;

/**
 * @author gauth
 *
 *         DTOxxx : class json != class JS JSxxx : class JS
 */
public interface JSPerfVuesJS extends JSClass, IJSONBuilder, IJSNodeTemplate, IJSDataDriven {

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
		JSInt i = declareType(JSInt.class, "i");

		/*********************************************************/
		JSArray<JSObject> m = new JSArray<JSObject>().asLitteral();
		JSObject phrase1 = new JSObject().asLitteral();
		phrase1.attr("a").set(JSString.value("aaaa"));
		m.push(phrase1);
		JSObject obja = new JSObject().asLitteral();
		obja.attr("m").set(m);
		
		let("jso", obja);
		
		/*********************************************************/
		if (isVueJS()) {

			JSArray<Object> list = new JSArray<>().asLitteral();

			// creer un json en java
			JSLambda doJava = () -> {
				DtoUser user = newJS(DtoUser.class);
				user.firstName().set("toto");
				list.push(user);

				list.push("test");
				list.push(1);
				list.push(1.2);

				JSArray<Object> list2 = new JSArray<>();
				list2.push("A");
				list2.push("B");

				list.push(list2);

				user = newJS(DtoUser.class);
				user.firstName().set(15151);
				list.push(user);
			};

			callJava(doJava);

			JSArray<?> json = let(JSArray.class, "json", list.getStringJSON());

			JSArray<?> json2 = let(JSArray.class, "json2",
					arr(obj( // version literral [{"a":1,"b":"aaa","c":{"a":1,"b":2},"d":[1,2,3]}]
							v("a", 1),
							v("b", "aaa"),
							v("c", obj(v("a", 1), v("b", 2))),
							v("d", arr(1, 2, 3)))));
			JSArray<?> json3 = let(JSArray.class, "json3", arr("a", "b", 1)); // ['a','b',1]

			DtoTest d = let("d", newJS(DtoTest.class).asLitteral()); // asLitteral: ne create pas le new DtoTest()

			d.users().set(newJS(JSArray.class)); // d.users=new Array()
			d.users().set(new JSArray<>().asLitteral()); // d.users=[]

			JSObject a = let("a1", newJS(JSObject.class)); // let a=new Object()
			JSObject b = let("b", newJS(JSObject.class).asLitteral()); //

			a.set(json);
			b.set(json2);
			b.set(json3);

					
			JSString id = let("id", JSString.value("testid"));
			JSString varText = let("varText", JSString.value("testVar"));
			JSString varText2 = let("varText2", JSString.value("binding"));

			JSArray<DtoUser> listUser = let("listUser", new JSArray<DtoUser>().asLitteral());

			DtoUser aUser = declareType(DtoUser.class, "aUser");
			JSon aDom = declareType(JSon.class, "aDom");

			CSSClass cA = new CSSClass().setId("cA");
			
			a.set(createNodeTemplate(
					xLi(
							xUl(xId(id), cA),
							xDiv(
									xDataDriven(listUser, 
											onEnter(aUser, xDiv(aUser.firstName())),
											onExit(aUser, aDom)  // TODO ici mettre une promise avant retirer le dom
										)
								),
							xDiv(varText,
									xH1(varText2)))));
			

			forIdx(i, 0, 50)._do(() -> {
				DtoUser auser = newJS(DtoUser.class).asLitteral();
				auser.firstName().set(txt("nickel", i));
				listUser.push(auser);
			});
			
			setTimeout(	() -> forIdx(i, 0, 50)._do(listUser::pop), 2000);

			__("document.getElementById('users').appendChild(", a, ")");
			
			_return();
			/******************************************************/

			String xid = "#app";

			__("new Vue({ el: '" + xid + "', data: ", d, " })");

			forIdx(i, 0, 20000)._do(() -> {
				DtoUser user = newJS(DtoUser.class).asLitteral(); // pas de let et pas de declaration de class
				user.firstName().set(txt("vuejs ", i));
				d.users().push(user);
			});

			if (isChange()) {
				setTimeout(() -> forIdx(i, 0, 20000)._do(
						() -> {
							d.users().at(i).firstName().set(txt("vuejs ", calc("20000-", i)));
							setTimeout(
									() -> forIdx(i, 0, 20000)._do(() -> d.users().pop()),
									2000);
						}),
						100);
			}

		} else if (isJQuery()) {
			JQuery jqUsers = let("jqUsers", $("#users"));

			JSString d = let("d", JSString.value("")); // new JSString()
			forIdx(i, 0, 20000)._do(
					() -> {
						d.append(txt("<li id='q", i, "'>que", i, "</li>"));
					});

			jqUsers.append(d);

			if (isChange()) {
				JQuery jqchildren = let("jqchildren", jqUsers.children());
				setTimeout(
						() -> forIdx(i, 0, 20000)._do(
								() -> jqchildren.eq(i).text(txt("que", calc("20000-", i)))),
						100);
			}

		} else {
			JSAny users = let(JSAny.class, "users", "document.getElementById('users')");

			forIdx(i, 0, 20000)._do(() -> {
				_var("node", "document.createElement('li')");
				_var("textnode", "document.createTextNode('dom'+i)");
				__("node.appendChild(textnode);");
				__(users, ".appendChild(node);");
			});

			if (isChange()) {
				JSArray<?> children = let(JSArray.class, "children", "document.getElementById('users').childNodes");
				setTimeout(
						() -> forIdx(i, 3, 20000)._do(
								() -> __(children.at(i), ".innerText='dom'+(20000-i)")),
						100);
			}
		}

	}
}
