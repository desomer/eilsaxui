/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import static com.elisaxui.component.toolkit.JQuery.$;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSAnonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.json.JSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import static com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplateImpl.onEnter;

/**
 * @author gauth
 *
 *         DTOxxx : class json != class JS JSxxx : class JS
 */
public interface JSPerfVuesJS extends JSClass, JSONBuilder, IXHTMLTemplate {

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
		JSInt j = JSClass.declareType(JSInt.class, "j");
		JSArray<?> child = JSClass.declareType(JSArray.class, "child");
		JSon elem = JSClass.declareType(JSon.class, "elem");
		JSon eldom = JSClass.declareType(JSon.class, "eldom");

		if (isVueJS()) {

			JSArray<Object> list = new JSArray<>().asLitteral();

			// creer un json en java
			JSAnonym doJava = () -> {
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

			DtoTest d = let("d", newInst(DtoTest.class).asLitteral()); // asLitteral: ne create pas le new DtoTest()

			d.users().set(newInst(JSArray.class)); // d.users=new Array()
			d.users().set(new JSArray<>().asLitteral()); // d.users=[]

			JSObject a = let("a1", newInst(JSObject.class)); // let a=new Object()
			JSObject b = let("b", newInst(JSObject.class).asLitteral()); //

			a.set(json);
			b.set(json2);
			b.set(json3);

			let("doElem", callback(eldom, elem, () -> {
				_if("elem instanceof Element || elem instanceof Text").then(() -> { // nodeType = Node.TextNode
					__("eldom.appendChild(elem)");
				})._elseif("typeof(elem) === 'string' || elem instanceof String").then(() -> {
					__("eldom.appendChild(document.createTextNode(elem))");
				})._elseif("elem instanceof Function").then(() -> {
					let("r", "elem.call(elem, eldom)");
					__("doElem(eldom, r)");
				})._else(() -> {
					JSArray<?> el = cast(JSArray.class, "elem");
					_forIdx(i, el)._do(() -> {
						let("attr", el.at(i));
						__("eldom.setAttributeNode(attr)");
					});
				});
			}));

			let("e", fct("id", child, "attr").__(() -> {
				JSVariable newdom = let(JSVariable.class, "newdom", "document.createElement(id)");
				_if(child.isNotEqual(null)).then(() -> {
					_forIdx(j, child)._do(() -> {
						let("elem", child.at(j));
						__("doElem(newdom, elem)");
					});
				});
				_return(newdom);
			}));

			let("a", callback(child, () -> {
				JSon attr = let(JSon.class, "attr", null);
				JSArray<Object> ret = let("ret", new JSArray<>().asLitteral());
				_forIdx(j, child)._do(() -> {
					let("elem", child.at(j));
					_if(j.modulo(2).isEqual(0)).then(() -> {
						attr.set("document.createAttribute(elem)");
						ret.push(attr);
					})._else(() -> {
						attr.get("value").set("elem");
					});

				});
				_return(ret);
			}));

			let("t", callback(child, () -> {
				JSon text = let(JSon.class, "text", null);
				text.set("document.createTextNode(", child, ")");
				_return(text);
			}));

			XClass cA = new XClass().setId("cA");
			JSString id = let("id", JSString.value("testid"));
			JSString varText = let("varText", JSString.value("testVar"));
			JSString varText2 = let("varText2", JSString.value("binding"));

			JSArray<DtoUser> listUser = let("listUser", new JSArray<DtoUser>().asLitteral());

			DtoUser aUser = JSClass.declareType(DtoUser.class, "aUser");

			a.set(xTemplateJS(
					xLi(
							xUl(xId(id), cA),
							xDiv(
									xData(listUser, 
											onEnter(aUser, xDiv(aUser.firstName()))
										)
								),
							xDiv(varText,
									xH1(varText2)))));

			_forIdxBetween(i, 0, 50)._do(() -> {
				DtoUser auser = newInst(DtoUser.class).asLitteral();
				auser.firstName().set(txt("nickel", i));
				listUser.push(auser);
			});

			__("document.getElementById('users').appendChild(", a, ")");
			
			_return();
			/******************************************************/

			String xid = "#app";

			__("new Vue({ el: '" + xid + "', data: ", d, " })");

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

			JSString d = let("d", JSString.value("")); // new JSString()
			_forIdxBetween(i, 0, 20000)._do(
					() -> {
						d.append(txt("<li id='q", i, "'>que", i, "</li>"));
					});

			jqUsers.append(d);

			if (isChange()) {
				JQuery jqchildren = let("jqchildren", jqUsers.children());
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
