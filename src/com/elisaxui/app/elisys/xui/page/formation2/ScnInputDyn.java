/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
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
						.vProp(ViewInputText.pLabel, "Color")
						.vProp(ViewInputText.pValue, "FF00FF")),
				vPart(new ViewInputText()
						.vProp(ViewInputText.pLabel, "Font size"))));
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
						XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", inputelem + ".XuiBindInfo");
						_if(ddi, "!=null &&", ddi.row().notEqualsJS(null)).then(() -> {
							ddi.row().attrByString(ddi.attr()).set(inputelem.value());
						});
					});
				}));
			});

			/*******************************************************/
			JSNodeElement aDom = declareType(JSNodeElement.class, "aDom");
			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");

			JSFunction onChange = fct(() -> {
				JSString sel = let(JSString.class, "sel", "'[data-xui'+", changeCtx.property(), "+']'");
				JSArray<JSNodeElement> listNode = let("listNode", aDom.querySelectorAll(sel));
				listNode.setArrayType(JSNodeElement.class);
				JSInt i = declareType(JSInt.class, "i");
				_forIdx(i, listNode)._do(() -> {
					JSNodeElement elem = let("elem", listNode.at(i));
					XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", elem + ".XuiBindInfo");
					JSAny r = let("r", changeCtx.value());
					_if(ddi, "!=null &&", ddi.fct().notEqualsJS(null)).then(() -> {
						JSAny rf = let("rf", ddi.fct().call(elem, changeCtx));
						_if(rf.equalsJS(null)).then(() -> {
							_return();
						});
						
						r.set(rf);
					});
					
					elem.textContent().set(r);
				});
			});
			/*******************************************************/
			JSNodeElement dom = let("dom", document().querySelector(cMain));

			JSArray<ItemInput> listItem = let("listItem", new JSArray<ItemInput>());
			ItemInput item = declareType(ItemInput.class, "item");

			JSFunction fctReverse = fct(changeCtx, () -> {
				_return(cast(JSString.class, changeCtx.value()).split(txt()).reverse().join(txt()));
			});
			
			dom.appendChildTemplate(
					vFor(listItem, item, xListNode(
							vPart(new ViewInputText()   // template static ou dynamic
									.vProp(ViewInputText.pLabel, vChangeable(item.label()))
									.vProp(ViewInputText.pValue, vBindable(item, item.value()))),
							/**************************************************************************/
							xDiv("static => ", item.value(), " <="),
							/**************************************************************************/
							xDiv("changeable => ", xSpan(vChangeable(item.value())), " <="),
							/**************************************************************************/
							xDiv("reverse => ", xSpan(vChangeable(item, item.value(), fctReverse)), " <="),
							/*************************************************************************/
							xDiv("class => ", vOnChange(item, item.value(), fct(changeCtx, ()->{
								consoleDebug(txt("vOnChange "),changeCtx);
							})))
							),
					/*************************************************************************/
					onChange(changeCtx, aDom, onChange)));

			/*******************************************************/
			ItemInput newItem = newJS(ItemInput.class);
			newItem.label().set("toto");
			newItem.value().set("valeur");
			listItem.push(newItem);

			newItem = newJS(ItemInput.class);
			newItem.label().set("style");
			newItem.value().set("block");
			listItem.push(newItem);

			setTimeout(() -> {
				listItem.at(0).label().set(listItem.at(0).label().add(" OK"));
				listItem.at(1).label().set("yeah");
			}, 5000);
		}
	}

	public interface ItemInput extends JSType {
		JSString label();

		JSString value();
	}

}
