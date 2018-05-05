/**
 * 
 */
package com.elisaxui.doc.formation2;

import java.util.ArrayList;
import java.util.List;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnTemplatePage")
public class ScnTemplatePage extends XHTMLPart {

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		
		vProperty(CmpSection.pArticleH1, "article defaut");
		vProperty(CmpSection.pSectionH1, "section defaut");
		vProperty(CmpSection.pSectionH1+"SEC1", "section sec1");
		vProperty(CmpSection.pSectionH1+"SEC3", "section sec3");
		vProperty(CmpSection.pContentArticle+"SEC3", "un article surcharge 3");
		
		/*************************************************/
		/* liste static */
		List<XMLElement> listSection = new ArrayList<>();
		listSection.add(vPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC1")));
		listSection.add(vPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC2")
									.vProperty(CmpSection.pSectionH1, "section test 2")
									.vProperty(CmpSection.pArticleH1, "article test 2")));
		listSection.add(vPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC3")
									.vProperty(CmpSection.pArticleH1, "article test 3")
									.vProperty(CmpSection.pContentArticle, "un article 3")
									));
		/***********************************************/
		vProp(CmpPage.pContentHeader, "HEADER");
		vProp(CmpPage.pContentMain, listSection);
		vProp(CmpPage.pContentFooter, "FOOTER");
		
		vProp(CmpPage.pContentMain, vPart(new CmpSection()
				.vProperty(CmpSection.pArticleH1, "article test 4")
				.vProperty(CmpSection.pContentArticle, "un article 4")
		));
		
		vProp(CmpPage.pContentMain, vPart(new CmpSection()
				.vProperty(CmpSection.pSectionH1, "section 5")
				.vProperty(CmpSection.pArticleH1, "article test 5")
				.vProperty(CmpSection.pContentArticle, "un article 5 bis")
		));
		
		vProp(CmpPage.pContentNav, xUl(xLi("a"), xLi("b"), xLi("c")));
		
		return vPart(new CmpPage());
	}

	/********************************************************/
	// les components
	/********************************************************/
	static class CmpPage extends XHTMLPart {
		static VProperty pContentHeader;
		static VProperty pContentMain;
		static VProperty pContentNav;
		static VProperty pContentFooter;
		
		static CSSClass cPageContainer;
		
		@xTarget(HEADER.class)
		public XMLElement style() {
			return cStyle()
					  .path(cPageContainer).set("display:flex")
					  .andDirectChild(cStyle().path("*").set("flex:1 1 auto"));
					
		}
		
		@xTarget(CONTENT.class)
		public XMLElement xBar() {
			return xListNode(
					xHeader(pContentHeader),
					xDiv( cPageContainer,
							xNav(pContentNav),
							xMain(pContentMain)
					),
					xFooter(pContentFooter));
		}

	}
	
	static class CmpSection extends XHTMLPart {
		static VProperty pSectionH1;
		static VProperty pArticleH1;
		static VProperty pContentArticle;
		static CSSClass cSection;
		
		@xTarget(HEADER.class)
		public XMLElement style() {
			return cStyle().path(cSection).set("border:1px solid black");
		}
		
		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return  xSection( cSection,
								vIfExist(pSectionH1, xH1(pSectionH1)),
								xArticle(vIfExist(pArticleH1, xH1(pArticleH1)),	 pContentArticle ),
								xAside()
							);
		}

	}
}
