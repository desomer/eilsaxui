/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.elisys.xui.page.formation2.ScnDataDriven.JSTestDataDriven;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEvent;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeHTMLInputElement;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 */
@xFile(id = "ScnInput")
public class ScnInputDyn extends XHTMLPart {

	static CSSClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(JSDomBuilder.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("ScnInput"), xArticle(cMain,
				vPart(new ViewInputText()
						.vProperties(ViewInputText.pLabel, "Color")
						.vProperties(ViewInputText.pValue, "FF00FF")),
				vPart(new ViewInputText()
						.vProperties(ViewInputText.pLabel, "Font size"))));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}

	/********************************************************/
	// une class js avec template et datadriven
	/********************************************************/
	public interface JSTest extends JSClass, IJSDomTemplate, IJSDataBinding, IJSDataDriven {

		@xStatic(autoCall = true)
		default void main() {

			/*******************************************************/
			JSArray<JSString> listEventLitteral = new JSArray<JSString>().asLitteral();
			listEventLitteral.push(JSString.value("change"));
			listEventLitteral.push(JSString.value("click"));
			listEventLitteral.push(JSString.value("keyup"));
			listEventLitteral.push(JSString.value("input"));
			listEventLitteral.push(JSString.value("paste"));
			listEventLitteral.push(JSString.value("blur"));

			JSArray<JSString> listEvent = let("listEvent", listEventLitteral);

			JSEvent event = declareType(JSEvent.class, "event");
			JSInt idx = declareType(JSInt.class, "idx");
			_forIdx(idx, listEvent)._do(() -> {
				document().addEventListener(listEvent.at(idx), fct(event, () -> {
					_if(event.target().nodeName().equalsJS("INPUT")).then(() -> {
							JSNodeHTMLInputElement inputelem = let(JSNodeHTMLInputElement.class, "inputelem",
									event.target());
							XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", inputelem + ".datadriveninfo");
							JSString attr = let("attr", ddi.attr().split(".").at(1));
							ddi.row().attrByString(attr).set(inputelem.value());
					});
				}));
			});
			
			/*******************************************************/
			JSNodeElement aDom = declareType(JSNodeElement.class, "aDom");
			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");

			JSFunction onChange = fct(() -> {
				JSString sel = let(JSString.class, "sel", "'[data-xui'+",changeCtx.property(),"+']'");
				JSArray<JSNodeElement> listNode = let("listNode", aDom.querySelectorAll(sel));
				listNode.setArrayType(JSNodeElement.class);
				JSInt i = declareType(JSInt.class, "i");
				_forIdx(i, listNode)._do(() -> {
					listNode.at(i).textContent().set(changeCtx.value());
				});
			});
			/*******************************************************/
			JSNodeElement dom = let("dom", document().querySelector(cMain));
			
			JSArray<ItemInput> listItem = let("listItem", new JSArray<ItemInput>());
			ItemInput item = declareType(ItemInput.class, "item");
			
			dom.appendChild(createDomTemplate(xDiv(
					vFor(listItem, item, xListNode(
							vPart(new ViewInputText()
									.vProperties(ViewInputText.pLabel, item.label())
									.vProperties(ViewInputText.pValue, vBind(item, item.value()))),
							xDiv(vChangeable("value", item.value()))),
							onChange(changeCtx, aDom, onChange)))));

			/*******************************************************/
			ItemInput newItem = newJS(ItemInput.class);
			newItem.label().set("toto");
			newItem.value().set("valeur");
			listItem.push(newItem);

			newItem = newJS(ItemInput.class);
			newItem.label().set("style");
			newItem.value().set("block");
			listItem.push(newItem);
		}
	}

	public interface ItemInput extends JSType {
		JSString label();

		JSString value();
	}

}
