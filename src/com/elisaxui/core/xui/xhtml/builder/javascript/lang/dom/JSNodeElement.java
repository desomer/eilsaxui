/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class JSNodeElement extends JSAny implements IJSClassInterface {

	public JSNodeElement appendChild(Object... element) {

		if (element.length == 1 && element[0] instanceof XMLElement) {
			XMLElement e = (XMLElement) element[0];

			if (e.getName() == null) { // un elem xml sans nom de noeud  xElem
				// ajoute un tableau d'element xElem(vFor(....
				Object f = new JSFunction()._return(JSDomBuilder.MTH_ADD_PART + "([", e.getListInner(), "], this)");
				Object self = JSContent.cast(JSAny.class, this.toString());
				return callMth("appendChild", JSContent.cast(JSAny.class, f + ".bind(" + self + ")()"));
			}
		}
		return callMth("appendChild", element);
	}

	/**************************************************/
	public JSNodeElement remove() {
		return callMth("remove");
	}

	public JSString textContent() {
		return castAttr(new JSString(), "textContent");
	}

	public JSDomTokenList classList() {
		return castAttr(new JSDomTokenList(), "classList");
	}

	public JSString nodeName() {
		return castAttr(new JSString(), "nodeName");
	}

	public JSon dataset() {
		return castAttr(new JSon(), "dataset");
	}

	public JSon style() {
		return castAttr(new JSon(), "style");
	}

	public JSon getBoundingClientRect() {
		return callTyped(new JSon(), "getBoundingClientRect");
	}

	public JSInt scrollTop() {
		return castAttr(new JSInt(), "scrollTop");
	}

	public void addEventListener(Object event, JSFunction fct) {
		callMth("addEventListener", event, fct);
	}

	public JSArray<JSNodeElement> childNodes() {
		return castAttr(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "childNodes");
	}

	public JSArray<JSNodeElement> children() {
		return castAttr(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "children");
	}

	/*************************************************************/
	/**
	 *   document().querySelector(txt(CSSSelector.onPath("#", lastIntent.activitySrc())))
	 * @param selector
	 * @return
	 */
	public JSNodeElement querySelector(CSSSelector selector) {
		return callTyped(new JSNodeElement(), "querySelector", "" + selector);
	}

	/**
	 *   document().querySelector(txt(CSSSelector.onPath("#", lastIntent.activitySrc())))
	 * @param variable
	 * @return
	 */
	public JSNodeElement querySelector(JSAny variable) {
		return callTyped(new JSNodeElement(), "querySelector", variable);
	}

	/**
	 *   document().querySelector(CssTransition.activity, CssTransition.active)
	 * @param selector
	 * @return
	 */
	public JSNodeElement querySelector(Object... selector) {
		if (selector.length == 1 && selector[0] instanceof JSAny)
			return querySelector((JSAny) selector[0]);
		else
			return querySelector(CSSSelector.onPath(selector));
	}

	/*************************************************************/
	public JSArray<JSNodeElement> querySelectorAll(CSSSelector selector) {
		return callTyped(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "querySelectorAll",
				"" + selector);
	}

	public JSArray<JSNodeElement> querySelectorAll(JSAny variable) {
		return callTyped(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "querySelectorAll", variable);
	}

	public JSArray<JSNodeElement> querySelectorAll(Object... selector) {
		if (selector.length == 1 && selector[0] instanceof JSAny)
			return querySelectorAll((JSAny) selector[0]);
		else
			return querySelectorAll(CSSSelector.onPath(selector));
	}

	/*************************************************************/
	public JSNodeElement closest(CSSSelector selector) {
		return callTyped(new JSNodeElement(), "closest", "" + selector);
	}

	public JSNodeElement closest(JSAny variable) {
		return callTyped(new JSNodeElement(), "closest", variable);
	}

	public JSNodeElement closest(Object... selector) {
		if (selector.length == 1 && selector[0] instanceof JSAny)
			return closest((JSAny) selector[0]);
		else
			return closest(CSSSelector.onPath(selector));
	}

	/***********************************************************/

	public <E extends JSAny> E firstNodeValue() {
		return this.childNodes().at(0).attr("nodeValue");
	}

}
