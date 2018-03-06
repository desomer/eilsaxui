/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.app.elisys.xui.page.formation2.ScnDataDriven.TestData;
import com.elisaxui.app.elisys.xui.page.formation2.part.CmpBtnBar;
import com.elisaxui.app.elisys.xui.page.formation2.part.CmpButton;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
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

import static com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplateImpl.onEnter;
import static com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplateImpl.onExit;

/**
 * @author gauth
 *
 */
@xFile(id = "ScnTemplatePart")
public class ScnTemplatePart extends XHTMLPart {

	static XClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return xListElem(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(JSXHTMLTemplate.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}
	
	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("Scn Template Part"), xDiv(cMain,
				xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, "OK"))));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}
	
	public interface JSTest extends JSClass, IXHTMLTemplate, IJSONBuilder {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {

			JSString label = let("label", JSString.value("OK 2"));
			JSString label2 = let("label2", JSString.value("OK 23"));
			JSString label3 = let("label3", JSString.value("OK 233"));

			JSFunction f = fct(() -> {
				_return(jsTemplate(xDiv("super")));
			});
			
			JSArray<JSObject> arr = let("arr", new JSArray<JSObject>().asLitteral());
			
			JSon aRow = declareType(JSon.class, "aRow");
			JSDomElement aDom = declareType(JSDomElement.class, "aDom");
			
			// btn data driven
			XMLElement btnOnDataDriven = xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, aRow.attr("t")));
			
			Object barDataDriven = xListElem(xPart(new CmpBtnBar(), 
					xDataDriven(arr, 
							onEnter(aRow,  btnOnDataDriven), 
							onExit(aRow, aDom))));
			
			// list element
			XMLElement listElem = xListElem(f, xDiv("LLL"), barDataDriven);
			
			// appel meth de JSDomElement
			JSDomElement ko = let("ko", xPicture(JSString.value("KO")));
			
			document().querySelector(cMain).appendChild(
					jsTemplate(
							xListElem(
									xPart(new CmpBtnBar()
											.addProperty("toto", "marche")
											.addProperty("titi", label3)
											.addProperty("test", listElem)
											, ko
											, xPart(new CmpBtnBar(),
													xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, label)),
													xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, label2)),
													xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, searchProperty("toto")))
											)
											, xH1(searchProperty("titi")) 
											, searchProperty("test")
											)
									, xH1("h1")
									)
							)
					);
			
			JSObject obj = new JSObject().asLitteral();
			obj.attr("t").set(JSString.value("11<hr>"));
			arr.push(obj);
			obj = new JSObject().asLitteral();
			obj.attr("t").set(JSString.value("12<hr>"));
			arr.push(obj);
			obj = new JSObject().asLitteral();
			obj.attr("t").set(JSString.value("13<hr>"));
			arr.push(obj);
		}
		
		@xStatic
		default JSDomElement xPicture(JSAny url) {
			return jsTemplate(xPart(new CmpButton().addProperty(CmpButton.PROPERTY_LABEL, url)));
		}

	}

}
