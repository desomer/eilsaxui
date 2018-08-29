/**
 * 
 */
package com.elisaxui.doc.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.XHTMLPartJS;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnTemplatePageDyn")
public class ScnTemplatePageDyn extends XHTMLPartJS implements IJSDataDriven, IJSMountFactory {
	
	/********************************************************/
	// les includes
	/********************************************************/
	@xTarget(HEADER.class)
	@xResource
	/**TODO a gerer en automatique if script ou style */
	public XMLElement xImport() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xElem(JSDomBuilder.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	/********************************************************/
	// la vue App Shell
	/********************************************************/
	@xTarget(CONTENT.class) 
	public XMLElement xAppShell() {
		/******** INIT PROPERTY ************************/
		vProp(CmpPage.pContentMain, vPart(new CmpSection()
				.vProp(CmpSection.pSectionH1, "section static")
				.vProp(CmpSection.pArticleH1, "article static")
				.vProp(CmpSection.pContentArticle, "un texte article static")));

		CmpNavVertical nav = new CmpNavVertical();
		vProp(CmpPage.pContentNav, vPart(nav,
				nav.xItem("a1"),
				nav.xItem("a2"),
				nav.xItem("a3")));

		/******** INIT PAGE ************************/
		return vPart(new CmpPage());
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xIncludeJS(JSController.class);
	}

	/********************************************************/
	// une class js CONTROLEUR avec template et datadriven
	/********************************************************/
	public interface JSController extends JSClass, IJSNodeTemplate {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			/******************* LES CONTENEURS ********************/
			
			JSArray<Section> arrSection = let("arrSection", new JSArray<Section>().asLitteral());
			JSArray<Menu> arrNav = let("arrNav", new JSArray<Menu>().asLitteral());

			/******************* LES FACTORY **********************/
			
			document().querySelector(CmpPage.cPageMain).appendChild(new ScnTemplatePageDyn().xSessionDyn(arrSection));
			document().querySelector(CmpNavVertical.cNavContainer).appendChild(new CmpNavVertical().xNavDyn(arrNav));

			/*******************  LES DATAS ************************/
			Section section = newJS(Section.class);
			section.titre().set("section A");
			section.article().set("un article a ajouter");
			section.phrases().set(new JSArray<JSObject>().asLitteral());
			arrSection.push(section);

			section = newJS(Section.class);
			section.titre().set("section B");
			section.article().set("un article 2 a ajouter");
			section.phrases().set(new JSArray<JSObject>().asLitteral());
			arrSection.push(section);

			/***************************************************/
			setTimeout(() -> {
				Phrase phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 1 ajouter");
				arrSection.at(0).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 2 ajouter");
				arrSection.at(0).phrases().push(phrase);
			}, 1000);

			setTimeout(() -> {
				Phrase phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 21 ajouter");
				arrSection.at(1).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 22 ajouter");
				arrSection.at(1).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 23 ajouter");
				arrSection.at(1).phrases().push(phrase);
			}, 2000);
			
			setTimeout(() -> {
				Menu menu = newJS(Menu.class);
				menu.titre().set("page A");
				arrNav.push(menu);

				menu = newJS(Menu.class);
				menu.titre().set("page B");
				arrNav.push(menu);
			}, 3000);

			/***************************************************/
			
			setTimeout(() -> {
				arrNav.pop();
				arrSection.pop();
			}, 4000);
			
			/***************************************************/
		}
	}

	/********************************************************/
	// les dto
	/********************************************************/
	public interface Menu extends JSType {
		JSString titre();
	}

	public interface Section extends JSType {
		JSString titre();

		JSString article();

		JSArray<Phrase> phrases();
	}

	public interface Phrase extends JSType {
		JSString text();
	}

	/********************************************************/
	// les components
	/********************************************************/
	// les variables de composant section
	static Section aSection;
	static Phrase aPhrase;
	// les variables de composant navbar
	static Menu aMenu;

	/********************************************************/
	/** session = titre + article + Phrases */
	public XMLElement xSessionDyn(JSArray<Section> arr) {
		return xListNode(
				vFor(arr, aSection, vPart(new CmpSection()
						.vProp(CmpSection.pSectionH1, aSection.titre())
						.vProp(CmpSection.pArticleH1, aSection.article())
						.vProp(CmpSection.pContentArticle,
								vFor(aSection.phrases(), aPhrase, xDiv(aPhrase.text()))))));
	}
	
	/********************************************************/
	static class CmpPage extends XHTMLPart {
		static VProperty pContentMain;
		static VProperty pContentNav;
		
		static CSSClass cPageContainer;
		static CSSClass cPageMain;

		@xTarget(HEADER.class)
		@xResource
		public XMLElement doStyle() {
			return cStyle()
					.path(cPageContainer).set("display:flex")
					.andDirectChild(cStyle().path("*").set("flex:1 1 auto"));
		}

		@xTarget(CONTENT.class)
		public XMLElement xBar() {
			return xListNode(
					xHeader(),
					xDiv(cPageContainer,
							xNav(pContentNav),
							xMain(cPageMain, pContentMain)),
					xFooter());
		}

	}

	/********************************************************/
	static class CmpSection extends XHTMLPart {
		static VProperty pSectionH1;
		static VProperty pArticleH1;
		static VProperty pContentArticle;
		static CSSClass cSection;

		@xTarget(HEADER.class)
		@xResource
		public XMLElement doStyle() {
			return cStyle().path(cSection).set("border:1px solid black");
		}

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xSection(cSection,
					vIfExist(pSectionH1, xH1(pSectionH1)),
					xArticle(vIfExist(pArticleH1, xH1(pArticleH1)),
							pContentArticle),
					xAside());
		}

	}

	/********************************************************/
	static class CmpNavVertical extends XHTMLPartJS implements IJSDataDriven, IJSMountFactory {
		static CSSClass cNavContainer;
		static VProperty pContent;

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xUl(cNavContainer, getChildren(), pContent);
		}

		public XMLElement xNavDyn(JSArray<Menu> arr) {
			return xListNode(vFor(arr, aMenu, xItem(aMenu.titre())));
		}

		public XMLElement xItem(Object content) {
			return xLi(content);
		}
	}
}
