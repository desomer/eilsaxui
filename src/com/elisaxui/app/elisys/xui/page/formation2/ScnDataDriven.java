/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 */
@xResource(id = "ScnDataDriven")
public class ScnDataDriven extends XHTMLPart {

	static CSSClass cMain;

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
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
		return xDiv(xH1("LOGO"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	@xResource // une seule fois par vue
	public XMLElement xLoad() {
		return xImport(JSTestDataDriven.class);
	}

	// une class JS
	@xTarget(AFTER_CONTENT.class) // une seule fois par vue car class
	public interface JSTestDataDriven extends JSClass, IJSDomTemplate, IJSDataDriven {

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

			// change les text en asynchrone tout les i*50ms
			_forIdxBetween(i, 0, 100)._do(
					() -> setTimeout(fct(j, () -> {
						listData.at(j).name().set(txt("wor ", j));
						listData.at(j).oneToOne().name2().set(txt("changeone ", j));
					}),
							calc(i, "*50"), i));

		}

		// le controleur
		default Object xArray(JSArray<TestData> data) {

			TestData aTestData = declareType(TestData.class, "aTestData");
			JSNodeElement aDom = declareType(JSNodeElement.class, "aDom");
			JSChangeCtx changeCtx = declareType(JSChangeCtx.class, "changeCtx");

			JSTestDataDriven that = let(JSTestDataDriven.class, "that", _this());

			
			JSFunction onChange = fct(() -> {
				_if(changeCtx.property().equalsJS("name")).then(
						() -> aDom.firstNodeValue().set(changeCtx.value()));
				_if(changeCtx.property().equalsJS("name2")).then(
						() -> aDom.querySelector("span").firstNodeValue().set(changeCtx.value()));
			});

			JSNodeElement aNewDomRow = that.xRow(aTestData.name(), aTestData.oneToOne().name2());
			
			return createDomTemplate(
					xUl(
							xDataDriven(data,
									onEnter(aTestData, aNewDomRow)	,
									onExit(aTestData, aDom),
									onChange(changeCtx, aDom, onChange))));

		}
		
		// le template
		default JSNodeElement xRow(JSString text, JSString id) {
			return createDomTemplate(xLi(text, xSpan(id)));
		}

	}

	// les dto
	public interface TestData extends JSType {
		JSString id();

		JSString name();

		TestDataRow oneToOne();

		JSArray<TestDataRow> oneToMany();
	}

	public interface TestDataRow extends JSType {
		JSString name2();
	}

}
