/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSONType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSXHTMLTemplate;
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
@xFile(id = "ScnDataDriven")
public class ScnDataDriven extends XHTMLPart {

	static XClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return xList(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(JSXHTMLTemplate.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("LOGO"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	@xRessource // une seule fois par vue
	public XMLElement xLoad() {
		return xImport(JSTestDataDriven.class);
	}

	// une class JS
	@xTarget(AFTER_CONTENT.class) // une seule fois par vue car class
	public interface JSTestDataDriven extends JSClass, IXHTMLTemplate, IJSDataDriven {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {

			JSInt i = declareType(JSInt.class, "i");
			JSInt j = declareType(JSInt.class, "j");
			/*************************************************************************/

			JSTestDataDriven template = let("template", newJS(JSTestDataDriven.class));
			JSArray<TestData> listData = let("listData", new JSArray<TestData>().asLitteral());

			document().querySelector(cMain).appendChild(template.xArray(listData));

			/*************************** CHARGEMENT DES JSON *************************/

			// ajout les ligne
			_forIdxBetween(i, 0, 100)._do(() -> {
				TestDataRow datarow = newJS(TestDataRow.class).asLitteral();
				datarow.name2().set(txt("one", i));
				
				TestData row = newJS(TestData.class).asLitteral();
				row.name().set(txt("row ", i));
				row.id().set(i);			
				row.oneToOne().set(datarow);
				row.oneToMany().set(new JSArray<>().asLitteral());
				
				listData.push(row);
			});

			// change les text en asynchrone
			_forIdxBetween(i, 0, 100)._do(
					() -> setTimeout(fct(j, () -> {
						listData.at(j).name().set(txt("wor ", j));
						listData.at(j).oneToOne().name2().set(txt("changeone ", j));
					}),
							calc(i, "*50"), i));

		}

		// le template
		default Object xArray(JSArray<TestData> data) {

			TestData aTestData = declareType(TestData.class, "aTestData");
			JSDomElement aDom = declareType(JSDomElement.class, "aDom");
			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");

			JSTestDataDriven that = let(JSTestDataDriven.class, "that", _this());

			
			JSFunction onChange = fct(() -> {
				_if(changeCtx.property().isEqual("name")).then(
						() -> aDom.firstNodeValue().set(changeCtx.value()));
				_if(changeCtx.property().isEqual("name2")).then(
						() -> aDom.querySelector("span").firstNodeValue().set(changeCtx.value()));
			});

			JSDomElement row = that.xRow(aTestData.name(), aTestData.oneToOne().name2());
			
			return jsTemplate(
					xUl(
							xDataDriven(data,
									onEnter(aTestData, row)	,
									onExit(aTestData, aDom),
									onChange(changeCtx, aDom, onChange))));

		}

		default JSDomElement xRow(JSString text, JSString id) {
			return jsTemplate(xLi(text, xSpan(id)));
		}

	}

	public interface TestData extends JSONType {
		JSString id();

		JSString name();

		TestDataRow oneToOne();

		JSArray<TestDataRow> oneToMany();
	}

	public interface TestDataRow extends JSONType {
		JSString name2();
	}

}
