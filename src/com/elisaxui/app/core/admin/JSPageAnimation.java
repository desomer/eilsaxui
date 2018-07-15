/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.component.toolkit.transition.CssTransition.active;
import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFixedForFreeze;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateNoDisplay;
import static com.elisaxui.component.toolkit.transition.CssTransition.inactive;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDomTokenList;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 *
 *         https://stackoverflow.com/questions/10592823/how-to-reverse-engineer-a-webkit-matrix3d-transform
 *         https://developer.apple.com/documentation/webkitjs/webkitcssmatrix
 *         https://stackoverflow.com/questions/19675997/how-to-exact-set-a-dynamic-style-transform-using-javascript
 *         https://stackoverflow.com/questions/8517173/change-image-opacity-using-javascript
 *         https://stackoverflow.com/questions/18574576/halting-a-translate3d-transition
 *         https://stackoverflow.com/questions/42267189/how-to-get-value-translatex-by-javascript
 *         https://css-tricks.com/controlling-css-animations-transitions-javascript/
 *
 */

@xCoreVersion("1")
@xExport
public interface JSPageAnimation extends JSClass {

	public static final String DATA_SCROLLTOP = "scrolltop";

	public static final String HIDDEN = "hidden";
	public static final String VISIBILITY = "visibility";
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String ABSOLUTE = "absolute";
	public static final String POSITION = "position";
	public static final String TOP = "top";
	public static final String BOTTOM = "bottom";
	public static final String OVERFLOW = "overflow";
	public static final String TRANSITION = "transition";
	public static final String TRANSFORM = "transform";

	public static final JSInt MEM_SCROLL = JSInt.value(-1);
	public static final JSInt ZERO = JSInt.value(0);

	JSDomTokenList classes = JSClass.declareType();
	JSInt idx = JSClass.declareType();
	JSNodeElement actContent = JSClass.declareType();
	JSArray<JSNodeElement> listfixedElem = JSClass.declareTypeArray(JSNodeElement.class);
	JSInt scrposition = JSClass.declareType();
	JSInt posTop = JSClass.declareType();
	JSNodeElement aElemNode = JSClass.declareType();

	default void doActivityActive(JSNodeElement activity) {
		let(classes, activity.classList());
		classes.remove(cStateNoDisplay);
		classes.add(active);
		classes.remove(inactive);
	}

	/***************************************************************************/

	default void doInitScrollTo(JSNodeElement activity) {
		let(scrposition, activity.dataset().attr(DATA_SCROLLTOP));
		window().scrollTo("0px", calc(scrposition, "==null?0:", scrposition));
	}

	/***************************************************************************/

	default void doActivityFreeze(JSNodeElement activity, JSInt sct) {
		let(classes, activity.classList());
		classes.add(cStateFixedForFreeze);

		_if(sct.equalsJS(MEM_SCROLL)).then(() -> {
			sct.set(activity.dataset().attr(DATA_SCROLLTOP));
			sct.set(calc(sct, "==null?0:", sct)); // met à 0 si null
		});

		activity.dataset().attr(DATA_SCROLLTOP).set(sct); // sauvegarde scroll position

		let(actContent, activity.querySelector(ViewPageLayout.getcContent()));
		// freeze
		actContent.style().attr(OVERFLOW).set(txt(HIDDEN)); // fait clignoter en ios
		actContent.style().attr(HEIGHT).set(txt("100vh"));
		actContent.scrollTop().set(sct);
	}

	default void doActivityDeFreeze(JSNodeElement activity) {
		let(actContent, activity.querySelector(ViewPageLayout.getcContent()));

		actContent.style().attr(OVERFLOW).set(txt());
		actContent.style().attr(HEIGHT).set(txt());

		let(classes, activity.classList());
		classes.remove(cStateFixedForFreeze);
	}

	/******************************************************************/
	default void doFixedElemToAbsolute(JSNodeElement act) {
		let(listfixedElem, act.querySelectorAll(cFixedElement));

		_forIdx(idx, listfixedElem)._do(() -> {

			let(aElemNode, listfixedElem.at(idx));
			let(classes, aElemNode.classList());

			_if(classes.contains(ViewTabBar.cFixedBottom)).then(() -> {
				// cas des node en bas de page
				aElemNode.style().attr(BOTTOM).set(txt("0px"));
			})._else(() -> {
				// cas des autres node fixed (ex floatingAction)			
				let(posTop, aElemNode.getBoundingClientRect().attr("y"));

				_if(posTop, ">0")
						.then(() -> aElemNode.style().attr(TOP).set(txt(posTop, "px")));
			});

			aElemNode.style().attr(POSITION).set(txt(ABSOLUTE));
		});
	}

	default void doFixedElemToFixe(JSNodeElement act) {
		let(listfixedElem, act.querySelectorAll(cFixedElement));

		_forIdx(idx, listfixedElem)._do(() -> {
			listfixedElem.at(idx).style().attr(TOP).set(txt());
			listfixedElem.at(idx).style().attr(POSITION).set(txt());
			listfixedElem.at(idx).style().attr(BOTTOM).set(txt());
		});
	}

	/********************************************************************/
}
