/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.widget.input.ViewCheckRadio;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xImport;
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
	@xImport(export = "JSDataDriven", module = "xdatadriven.js")
	@xImport(export = "JSDataBinding", module = "xBinding.js")

	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}

	public interface JSTest extends JSClass, IJSDomTemplate, IJSDataBinding, IJSDataDriven {

		@xStatic(autoCall = true)
		default void main() {
			JSAppConfiguration item = declareType(JSAppConfiguration.class, "item");

			JSArray<JSAppConfiguration> data = let("data", new JSArray<JSAppConfiguration>());
			JSAppConfiguration row = let("row", newJS(JSAppConfiguration.class));
			row.minify().set(true);

			JSNodeElement dom = let("dom", document().querySelector(cMain));
			dom.appendChildTemplate(
					vFor(data, item, xListNode(
							vPart(new ViewCheckRadio().vProp(ViewCheckRadio.pLabel, "Minify")
//									.vProp(ViewCheckRadio.pValue, vIf(vBindable(item, item.minify()), new JSNodeTemplate(xDiv("'OK'")).setModeJS(true)))),
									.vProp(ViewCheckRadio.pValue, vIf(vBindable(item, item.minify()), xAttr("checked")))),

							vPart(new ViewCheckRadio().vProp(ViewCheckRadio.pLabel, "ES5 Compatibility")),
							vPart(new ViewCheckRadio().vProp(ViewCheckRadio.pLabel, "Disable comment")),
							vPart(new ViewCheckRadio().vProp(ViewCheckRadio.pLabel, "Single File")),
							vPart(new ViewInputText() 
									.vProp(ViewInputText.pLabel, "Color")
									.vProp(ViewInputText.pValue, txt("FF00FF"))),
							vIf(vBindable(item, item.minify()),xDiv("OK")),
							vPart(new ViewInputText()
									.vProp(ViewInputText.pLabel, "Font size")
									.vProp(ViewInputText.pValue, txt("12rem")))

					)));
			
			data.push(row);
		}
	}

	public interface JSAppConfiguration extends JSType {
		JSBool minify();
	}
}
