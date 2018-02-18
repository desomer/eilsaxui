/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;
import static com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplateImpl.onEnter;
import static com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplateImpl.onExit;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
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
@xFile(id = "ScnDataDriven2")
public class ScnDataDriven2 extends XHTMLPart {

	static XClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return xListElem(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(
						JSXHTMLTemplate.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("LOGO"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTestDataDriven.class);
	}

	// une class JS
	@xTarget(AFTER_CONTENT.class) // une seule fois par vue car class
	public interface JSTestDataDriven extends JSClass, IXHTMLTemplate, IJSONBuilder {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {

			JSInt i = declareType(JSInt.class, "i");

			JSTestDataDriven template = let("template", newInst(JSTestDataDriven.class));
			JSArray<TestData> container = let("container", new JSArray<TestData>().asLitteral());

			document().querySelector(cMain).appendChild(template.xRow(container));

			_forIdxBetween(i, 0, 100)._do(() -> {
				TestData row = newInst(TestData.class).asLitteral();
				row.name().set(txt("row ", i));
				container.push(row);
			});

		}

		default Object xRow(JSArray<TestData> data) {
			TestData aTestData = declareType(TestData.class, "aTestData");
			JSElement aDom = declareType(JSElement.class, "aDom");

			return xTemplateJS(xUl(xDataDriven(data,
					onEnter(aTestData, xLi(aTestData.name())),
					onExit(aTestData, aDom))));
		}

	}

	public interface TestData extends JSONType {
		JSString name();
	}

}
