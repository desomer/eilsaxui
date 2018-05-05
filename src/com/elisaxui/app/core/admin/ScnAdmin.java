/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleBinding;
import com.elisaxui.component.toolkit.com.JSCom;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.input.ViewCheckRadio;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xImport;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnAdmin")
public class ScnAdmin extends XHTMLPart {

	static CSSClass cMain;
	static CSSClass cBtnOk;

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportLib() {
		return xListNode(vPart(new CmpModuleBinding())); // configure et genere les modules
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("Admin"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	@xResource(id = "xLoad.js")
	@xImport(export = "TKCom", module = "xComPoly.js")
	@xImport(export = "JSDataDriven", module = "xdatadriven.js")
	@xImport(export = "JSDataBinding", module = "xBinding.js")
	public XMLElement xLoad() {
		return xInclude(JSTest.class);
	}

	public interface JSTest extends JSClass, IJSNodeTemplate, IJSDataBinding, IJSDataDriven {

		@xStatic(autoCall = true)
		default void main() {
			JSAppConfiguration item = declareType(JSAppConfiguration.class, "item");
			JSArray<JSAppConfiguration> data = let("data", new JSArray<JSAppConfiguration>());

			JSNodeElement dom = let("dom", document().querySelector(cMain));
			dom.appendChild(
					vFor(data, item, xListNode(
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Single File")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.singleFile()), xAttr("checked")))),
							/*****************************************************************************/
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Minify")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.minify()), xAttr("checked")))),
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "ES5 Compatibility")
									.vProp(ViewCheckRadio.pValue, vIf(vBindable(item, item.es5()), xAttr("checked")))),
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Disable comment")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.disableComment()), xAttr("checked")))),
							/*****************************************************************************/
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Display time generated")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.timeGenerated()), xAttr("checked")))),
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Display File Changed")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.fileChanged()), xAttr("checked")))),
							vPart(new ViewCheckRadio()
									.vProp(ViewCheckRadio.pLabel, "Display Patch Changes")
									.vProp(ViewCheckRadio.pValue,
											vIf(vBindable(item, item.patchChanges()), xAttr("checked")))),
							/****************************************************************************/
							vPart(new ViewInputText()
									.vProp(ViewInputText.pLabel, "Version time line")
									.vProp(ViewInputText.pValue, vBindable(item, item.versionTimeLine()))),
							/*****************************************************************************/
							vPart(new ViewInputText()
									.vProp(ViewInputText.pLabel, "Color")
									.vProp(ViewInputText.pValue, txt("FF00FF"))),
							vPart(new ViewInputText()
									.vProp(ViewInputText.pLabel, "Font size")
									.vProp(ViewInputText.pValue, txt("12rem"))),
							/*****************************************************************************/
							xNode("HR"),
							xButton(cBtnOk, "SAVE")

					)));

			JSCom.xuiCom().requestUrl(JSString.value(SrvAdmin.URL_CONFIG)).then(fct(item, () -> {
				data.push(item);
				
				JSon attrDomLink = cast(JSon.class, item).attrByString(JSDataSet.ATTR_DOM_LINK);  //inline
				
				setTimeout(fct(() -> {
					document().querySelector(cBtnOk).addEventListener("click", fct(() -> {

						JSNodeElement rowDom = let(JSNodeElement.class, "rowDom", attrDomLink );
						delete(attrDomLink);
						
						JSCom.xuiCom().postUrl(JSString.value(SrvAdmin.URL_CONFIG), item).then(fct(() -> {
							__("location.reload(true)");
						}));
						attrDomLink.set(rowDom);

					}));
				}), 1000);
			}));

		}
	}

	public interface JSAppConfiguration extends JSType {
		JSBool minify();

		JSBool es5();

		JSBool disableComment();

		JSBool singleFile();
		
		JSBool timeGenerated();
		JSBool fileChanged();
		JSBool patchChanges();
		
		JSInt versionTimeLine();
	}
}
