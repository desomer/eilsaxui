/**
 * 
 */
package com.elisaxui.doc.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleCore;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 * 
 * https://localhost:9998/rest/page/fr/fra/id/ScnInput
 *   
 */
@xResource(id = "ScnInput")
public class ScnInputDyn extends XHTMLPart implements ICSSBuilder {

	static CSSClass cMain;
	static CSSClass cSizeOk;

	@xTarget(AFTER_CONTENT.class)
	@xResource(id = "xScnInput.css", async=true)
	@xPriority(10)
	public XMLElement xStylePart() {
		return xElem(
				xStyle(sMedia("all"), () ->
					sOn(sSel(cSizeOk), () -> 
						css("color:red;")
						)
					)
				);
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("ScnInput"), xArticle(cMain,
				xElem(new ViewInputText() // template static
						.vProp(ViewInputText.pLabel, "Color")
						.vProp(ViewInputText.pValue, "FF00FF")),
				xElem(new ViewInputText()
						.vProp(ViewInputText.pLabel, "Font size"))));
	}

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportLib() {
		return xElem(new CmpModuleCore()); // configure et genere les modules
	}
	
	/********************************************************/
	// une class js avec template et datadriven
	/********************************************************/
	@xTarget(AFTER_BODY.class) // le controleur apres chargement du body
	@xResource(id = "xScnInput.mjs")
	@xImport(idClass = JSDataDriven.class)
	@xImport(idClass = JSDataBinding.class)
	public XMLElement xLoad() {
		return xElem(JSTest.class);
	}
	
	public interface JSTest extends JSClass, IJSNodeTemplate, IJSDataBinding, IJSDataDriven, IJSMountFactory {

		@xStatic(autoCall = true)
		default void main() {
			
			__("window.datadrivensync=true");

			// declaration des types
			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");
			ItemInput item = declareType(ItemInput.class, "item");

			// fct reverse des caracteres
			JSFunction fctReverse = fct(changeCtx, () -> 
					_return(cast(JSString.class, changeCtx.value()).split(txt()).reverse().join(txt())));

			// fct calcul le nb de caracteres
			JSFunction fctNbChar = fct(() -> _return(calc("'nb car '+", item.value().length())));

			// les datas
			JSArray<ItemInput> listItem = let("listItem", new JSArray<ItemInput>());

			// la vues dynamique
			JSNodeElement dom = let("dom", document().querySelector(cMain));
			dom.appendChild(xElem(
					vFor(listItem, item, xDiv(
							xElem(new ViewInputText() // template static ou dynamic
									.vProp(ViewInputText.pLabel, vChangeable(item.label()))
									.vProp(ViewInputText.pValue, vBindable(item, item.value()))),
							/**************************************************************************/
							xDiv("static => ", item.value(), " <="),
							/*************************************************************************/
							xDiv("static by fct => ", fctNbChar, " <="),
							/**************************************************************************/
							xDiv(vChangeable(item.value())),
							/*************************************************************************/
							xDiv("changeable => ", xSpan(vChangeable(item.value())), " <="),
							/**************************************************************************/
							xDiv("reverse => ", xSpan(vChangeable(item, item.value(), fctReverse)), " <="),
							/*************************************************************************/
							vOnDataChange(item,  /*????*/ item.value(), fct(changeCtx, () -> consoleDebug(txt("un event OnDataChange"), changeCtx))),
							/*************************************************************************/
							xDiv("Plus de 10 char", vOnDataChange(item, item.value(), fct(changeCtx,
									() -> _if(changeCtx.value().toStringJS().length(), ">10")
											.then(() -> changeCtx.element().classList().add(cSizeOk))
											._else(() -> changeCtx.element().classList().remove(cSizeOk))))),
							xNode("hr")
							))));

			/*******************************************************/
			// ajout des datas
			ItemInput newItem = newJS(ItemInput.class);
			newItem.label().set("border");
			newItem.value().set("5px solid red");
			listItem.push(newItem);

			newItem = newJS(ItemInput.class);
			newItem.label().set("display");
			newItem.value().set("block");
			listItem.push(newItem);

			// changement des datas
			setTimeout(() -> {
				listItem.at(0).label().set(listItem.at(0).label().add(" OK"));
				listItem.at(1).label().set("DISPLAY");
			}, 5000);
		}
	}

	public interface ItemInput extends JSType {
		JSString label();

		JSString value();
	}

}
