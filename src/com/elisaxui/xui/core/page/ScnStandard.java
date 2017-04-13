package com.elisaxui.xui.core.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

@xFile(id = "standard.html")
@xComment("activite standard")
public class ScnStandard extends XHTMLPart {

	@xTarget(HEADER.class)
	@xRessource
	public Element xTitle() {
		return xElement("title", "le standard");
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		int heightNavBar = 64;
		
		return xCss()
				.add("*", "-webkit-tap-highlight-color: rgba(0,0,0,0);}")
				.add("html", "font-size: 14px;line-height: 1.5;"
						+ "font-family: 'Roboto', sans-serif;font-weight: normal; color: rgba(0,0,0,0.87);")
				.add("body", "background-color: white;margin: 0")
				//.add("div", "color: black;")
				
				.add(".navbar","background-color: #ee6e73;height: "+heightNavBar+"px;width: 100%; color:white; "
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.add(".content", "padding: 8px; margin-top: "+heightNavBar+"px")
				.add(".center", "height:100%; display: flex; align-items: center;justify-content: center")
				.add(".logo", "color: inherit; font-size: 2.1rem;")
				
				.add(".fixedTop", "position:fixed; top:0px")
				
				.add(".panel", "padding: 15px;"
						+ "margin-bottom: 15px;"
						+ "border-radius: 0;"
						+ "background-color: #FFF;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12)")
				
				.add(".hamburger .hamburger-inner, "
						+ ".hamburger .hamburger-inner:after, "
						+ ".hamburger .hamburger-inner:before","background-color: #fff;")
				.add(".nooutline", "outline:0 !important")
				;
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportJQUERY() {
		return xElement("/", "<script  src='http://code.jquery.com/jquery-3.2.1.min.js'"
				+ "  integrity='sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4='  crossorigin='anonymous'></script>"
				+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css'>"
				);
	}

	
	public Element xBurgerBtn()
	{
		return xElement("button", xAttr("type","'button'"), xAttr("class", "'nooutline hamburger hamburger--elastic'"), xSpan(xAttr("class", "'hamburger-box'"), xSpan(xAttr("class", "'hamburger-inner'"))));
	}
	
	public Element xTest() {
		return xDiv(xAttr("class", "'panel'"),"Loin, très loin, au delà des monts Mots, à mille lieues des pays Voyellie et Consonnia, demeurent les Bolos Bolos. Ils vivent en retrait, à Bourg-en-Lettres, sur les côtes de la Sémantique, un vaste océan de langues. Un petit ruisseau, du nom de Larousse, coule en leur lieu et les approvisionne en règlalades nécessaires en tout genre; un pays paradisiagmatique, dans lequel des pans entiers de phrases prémâchées vous volent litéralement tout cuit dans la bouche. Pas même la toute puissante Ponctuation ne régit les Bolos Bolos - une vie on ne peut moins orthodoxographique. Un jour pourtant, une petite ligne de Bolo Bolo du nom de Lorem Ipsum décida de s'aventurer dans la vaste Grammaire. Le grand Oxymore voulut l'en dissuader, le prevenant que là-bas cela fourmillait de vils Virgulos, de sauvages Pointdexclamators et de sournois Semicolons qui l'attendraient pour sûr au prochain paragraphe, mais ces mots ne firent écho dans l'oreille du petit Bolo qui ne se laissa point déconcerter. Il pacqua ses 12 lettrines, glissa son initiale dans sa panse et se mit en route. Alors qu'il avait gravi les premiers flancs de la chaîne des monts Italiques, il jeta un dernier regard sur la skyline de");

	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xDiv(xAttr("class", "'navbar fixedTop'"), xBurgerBtn(),  xDiv(xAttr("class", "'center'"),xDiv(xAttr("class", "'logo'"),"le header"))), xDiv(xAttr("class", "'content'"), xTest(),xTest(),xTest(),xTest(),xTest()));	
			}

	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return 			
				xScriptJS(js().__("$('.hamburger').on('click', function(e) { $(this).toggleClass('is-active') })")
						);
	}
}
