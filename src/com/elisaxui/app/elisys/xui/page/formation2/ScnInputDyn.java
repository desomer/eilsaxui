/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
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
	public interface JSTest extends JSClass, IJSDomTemplate, IJSDataDriven {

		@xStatic(autoCall = true)
		default void main() {
			JSDomElement dom = let("dom", document().querySelector(cMain));

			JSArray<JSString> listEventLitteral = new JSArray<JSString>().asLitteral();
			listEventLitteral.push(JSString.value("change"));
			listEventLitteral.push(JSString.value("click"));
			listEventLitteral.push(JSString.value("keyup"));
			listEventLitteral.push(JSString.value("input"));
			listEventLitteral.push(JSString.value("paste"));
			listEventLitteral.push(JSString.value("blur"));
			
			JSArray<JSString> listEvent = let("listEvent", listEventLitteral);

			JSAny event = declareType(JSAny.class, "event");

			JSInt idx = declareType(JSInt.class, "idx");
			_forIdx(idx, listEvent)._do(() -> {
				document().addEventListener(listEvent.at(idx), fct(event, () -> {
					consoleDebug(event, event+".target.datadriveninfo");
				}));
			});

			JSArray<ItemInput> listItem = let("listItem", new JSArray<ItemInput>());
			
			ItemInput item = declareType(ItemInput.class, "item");
			JSDomElement domItem = declareType(JSDomElement.class, "domItem");
			
			dom.appendChild(createDomTemplate(xElem(
					vFor(listItem, item,
							vPart(new ViewInputText()
									.vProperties(ViewInputText.pLabel, item.label())
									.vProperties(ViewInputText.pValue, fct(domItem, ()->{ 
										__(domItem, ".datadriveninfo={row:", item ,", attr:'"+item.value()+"'}" );
										_return( item.value() );
									}) ))))));

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
