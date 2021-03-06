/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleCore;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.input.ViewCheckRadio;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
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
		return xElem(new CmpModuleCore()); // configure et genere les modules
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("Admin"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	@xResource(id = "xLoad.mjs")
	@xImport(idClass=TKCom.class)
	@xImport(idClass=JSDataDriven.class)
	@xImport(idClass=JSDataBinding.class)
	public XMLElement xLoad() {
		return xElem(JSTest.class);
	}

	@xCoreVersion("1")
	public interface JSTest extends JSClass, IJSNodeTemplate, IJSDataBinding, IJSDataDriven, IJSMountFactory {
		
		JSNodeElement dom = JSClass.declareType();
		JSNodeElement rowDom= JSClass.declareType();
		TAppConfiguration item= JSClass.declareType();
		JSArray<TAppConfiguration> data = JSClass.declareTypeArray(TAppConfiguration.class);
		
		@xStatic(autoCall = true)
		default void main() {

			TKCom tkCom = JSClass.declareTypeClass(TKCom.class);
			
			let(data, JSArray.newLitteral());
			let(dom, document().querySelector(cMain));

			dom.appendChild(xElem(vFor(data, item, xDiv(
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Single File")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.singleFile()), xAttr("checked")))),
					/*****************************************************************************/
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Minify")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.minify()), xAttr("checked")))),
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "ES5 Compatibility")
							.vProp(ViewCheckRadio.pValue, vIfOnce(vBindable(item, item.es5()), xAttr("checked")))),
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Disable comment")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.disableComment()), xAttr("checked")))),
					/*****************************************************************************/
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Display time generated")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.timeGenerated()), xAttr("checked")))),
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Display File Changed")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.fileChanged()), xAttr("checked")))),
					xElem(new ViewCheckRadio()
							.vProp(ViewCheckRadio.pLabel, "Display Patch Changes")
							.vProp(ViewCheckRadio.pValue,
									vIfOnce(vBindable(item, item.patchChanges()), xAttr("checked")))),
					/****************************************************************************/
					xElem(new ViewInputText()
							.vProp(ViewInputText.pLabel, "Version time line")
							.vProp(ViewInputText.pValue, vBindable(item, item.versionTimeLine()))),
					/*****************************************************************************/
					xElem(new ViewInputText()
							.vProp(ViewInputText.pLabel, "Color")
							.vProp(ViewInputText.pValue, txt("FF00FF"))),
					xElem(new ViewInputText()
							.vProp(ViewInputText.pLabel, "Font size")
							.vProp(ViewInputText.pValue, txt("12rem"))),
					/*****************************************************************************/
					xNode("HR"),
					xButton(cBtnOk, "SAVE")

			))));

			tkCom.requestUrl(JSString.value(SrvAdmin.URL_CONFIG)).then(fct(item, () -> {
				data.push(item);

				JSon attrDomLink = cast(JSon.class, item).attrByStr(JSDataSet.ATTR_DOM_LINK); // inline

				setTimeout(fct(() -> document().querySelector(cBtnOk).addEventListener("click", fct(() -> {
					let(rowDom, attrDomLink);
					delete(attrDomLink);

					tkCom.postUrl(JSString.value(SrvAdmin.URL_CONFIG), item)
							.then(fct(() -> __("location.reload(true)")));
					attrDomLink.set(rowDom);

				}))), 1000);
			}));

		}
	}

	public interface TAppConfiguration extends JSType {
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
