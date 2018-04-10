/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.annotation.xImport;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.target.MODULE;
/**
 * @author gauth
 */
@xResource(id = "ScnInput")
public class ScnInputDyn extends XHTMLPart implements ICSSBuilder {

	static CSSClass cMain;
	static CSSClass cSizeOk;

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportLib() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"));
	}

	/**************************************************************/
	@xTarget(HEADER.class)
	@xResource(id = "xdatadriven.js")
	public XMLElement xImportDataDriven() {
		return xListNode(
				xImport(
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class
						));
	}
	/**************************************************************/
	@xTarget(HEADER.class)
	@xResource(id = "xBinding.js")
	public XMLElement xImportBinding() {
		return xListNode(
				xImport(
						JSDomBuilder.class,
						JSDataBinding.class));
	}
	
	/**************************************************************/
	@xTarget(MODULE.class)
	@xResource(id = "FileImport.js") // gestion du required
	public XMLElement xImportFile() {
		return xListNode(xImport(JSTest2.class));
	}
	
	public interface JSTest2 extends JSClass{

		@xStatic(autoCall = true)
		default void main() {
			consoleDebug("'ok'");
		}
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xTextScriptImport() {
		return xNode("script", xAttr("type", "'module'"), js().__("import \"/rest/js/FileImport.js\""));
	}
	/***************************************************************/
	
	@xTarget(AFTER_CONTENT.class)
	@xResource(id = "xCss.css")
	@xPriority(200)
	public XMLElement xStylePart() {

		return xListNode(
				xStyle(sMedia("all"), () -> {
					sOn(sSel(cSizeOk), () -> {
						css("color:red;");
					});
				}));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("ScnInput"), xArticle(cMain,
				vPart(new ViewInputText() // template static
						.vProp(ViewInputText.pLabel, "Color")
						.vProp(ViewInputText.pValue, "FF00FF")),
				vPart(new ViewInputText()
						.vProp(ViewInputText.pLabel, "Font size"))));
	}
	
	/********************************************************/
	// une class js avec template et datadriven
	/********************************************************/
	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	@xResource(id = "xLoad.js") // gestion du required
	@xImport(module="xdatadriven.js")
	@xImport(module="xBinding.js")
	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}
	
	public interface JSTest extends JSClass, IJSDomTemplate, IJSDataBinding, IJSDataDriven {

		@xStatic(autoCall = true)
		default void main() {

			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");
			ItemInput item = declareType(ItemInput.class, "item");

			JSNodeElement dom = let("dom", document().querySelector(cMain));

			JSFunction fctReverse = fct(changeCtx,
					() -> _return(cast(JSString.class, changeCtx.value()).split(txt()).reverse().join(txt())));

			JSFunction fctNbChar = fct(() -> _return(calc("'nb car '+", item.value().length())));

			JSArray<ItemInput> listItem = let("listItem", new JSArray<ItemInput>());
			dom.appendChildTemplate(
					vFor(listItem, item, xListNode(
							vPart(new ViewInputText() // template static ou dynamic
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
							vOnDataChange(item, item.value(), fct(changeCtx, () -> consoleDebug(changeCtx))),
							/*************************************************************************/
							xDiv("Plus de 10 char", vOnDataChange(item, item.value(), fct(changeCtx,
									() -> _if(changeCtx.value().toStringJS().length(), ">10")
											.then(() -> changeCtx.element().classList().add(cSizeOk))
											._else(() -> changeCtx.element().classList()
													.remove(cSizeOk)))))))
			/*************************************************************************/
			);

			/*******************************************************/
			ItemInput newItem = newJS(ItemInput.class);
			newItem.label().set("border");
			newItem.value().set("5px solid red");
			listItem.push(newItem);

			newItem = newJS(ItemInput.class);
			newItem.label().set("display");
			newItem.value().set("block");
			listItem.push(newItem);

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
