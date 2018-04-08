/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder.XuiBindInfo;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
/** TODO a ajouter : le fct  IJSDataBinding */
public interface JSDataBinding extends JSClass {

	@xStatic
	default void initChangeHandler( JSChangeCtx changeCtx, JSNodeElement aDom)
	{
		/*******************************************************/

			JSString sel = let(JSString.class, "sel", "'[data-xui'+", changeCtx.property(), "+']'");
			JSArray<JSNodeElement> listNode = let(JSArray.class, "listNode", "Array.from(", aDom.querySelectorAll(sel), ")");
			_if(aDom, ".XuiBindInfo!=null").then(() -> {
				listNode.push(aDom);
			});
			listNode.setArrayType(JSNodeElement.class);
			JSInt i = declareType(JSInt.class, "i");
			_forIdx(i, listNode)._do(() -> {
				JSNodeElement elem = let("elem", listNode.at(i));
				XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", elem + ".XuiBindInfo");
				JSAny valueChange = let("valueChange", changeCtx.value());
				_if(ddi, "!=null &&", ddi.fct().notEqualsJS(null)).then(() -> {
					changeCtx.element().set(elem);
					JSAny rf = let("rf", ddi.fct().call(elem, changeCtx));  // appel de la fct vOnChange( fct )
					_if(rf.equalsJS(null)).then(() -> {
						__("continue");   // ne change pas la value du text du dom
					});

					valueChange.set(rf);  // la fct retour la valeur a changer
				});

				elem.textContent().set(valueChange);
			});
		;
		/*******************************************************/
	}
	
}
