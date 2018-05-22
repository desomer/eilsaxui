/**
 * 
 */
package com.elisaxui.component.widget.layout;

import static com.elisaxui.component.toolkit.transition.CssTransition.active;
import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFixedForFreeze;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateNoDisplay;
import static com.elisaxui.component.toolkit.transition.CssTransition.inactive;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDomTokenList;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;

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
public interface JSPageAnimation extends JSClass {

	public static final String DATA_SCROLLTOP = "scrolltop";

	public static final String HIDDEN = "hidden";
	public static final String VISIBILITY = "visibility";
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String ABSOLUTE = "absolute";
	public static final String POSITION = "position";
	public static final String TOP = "top";
	public static final String OVERFLOW = "overflow";
	public static final String TRANSITION = "transition";
	public static final String TRANSFORM = "transform";

	public static final JSInt MEM_SCROLL = JSInt.value(-1);
	public static final JSInt ZERO = JSInt.value(0);

	default void doActivityActive(JSNodeElement activity) {
		JSDomTokenList classes = let("classes", activity.classList());
		classes.remove(cStateNoDisplay);
		classes.add(active);
		classes.remove(inactive);
	}

	default void doActivityFreeze(JSNodeElement activity, JSInt sct) {

		JSDomTokenList classes = let("classes", activity.classList());
		classes.add(cStateFixedForFreeze);

		_if(sct.equalsJS(MEM_SCROLL)).then(() -> {
			sct.set(activity.dataset().attr(DATA_SCROLLTOP));
			sct.set(calc(sct, "==null?0:", sct)); // met à 0 si null
		});

		activity.dataset().attr(DATA_SCROLLTOP).set(sct); // sauvegarde scroll position

		JSNodeElement actContent = let("actContent", activity.querySelector(ViewPageLayout.getcContent()));
		// freeze
		actContent.style().attr(OVERFLOW).set(txt(HIDDEN)); // fait clignoter en ios
		actContent.style().attr(HEIGHT).set(txt("100vh"));
		actContent.scrollTop().set(sct);
	}

	default void doFixedElemToAbsolute(JSNodeElement act, JSInt sct) {
		JSInt idx = declareType(JSInt.class, "idx");

		JSArray<JSNodeElement> listfixedElem = let("listfixedElem", act.querySelectorAll(cFixedElement));
		listfixedElem.setArrayType(JSNodeElement.class);

		_forIdx(idx, listfixedElem)._do(() -> {
			JSInt posTop = let(JSInt.class, "posTop", sct);
			posTop.set(posTop.add(listfixedElem.at(idx), ".getBoundingClientRect().y"));
			_if(posTop, ">0")
					.then(() -> listfixedElem.at(idx).style().attr(TOP).set(txt(posTop, "px")));
			listfixedElem.at(idx).style().attr(POSITION).set(txt(ABSOLUTE));
		});
	}

}
