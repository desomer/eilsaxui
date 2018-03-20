/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.builder.css.CSSElement;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class ScnStyle implements ICSSBuilder {

	static CSSClass cAB = new CSSClass().setId("cAB");
	static CSSClass cABC = new CSSClass().setId("cABC");
	
	void doStyle()
	{
		
	    xStyle(sMedia("screen and (max-width: 640px)"), ()->{
			xStyle(sSel("a"), ()->{
				css("display:blocka");
				xStyle(sSel(XUIScene.scene.and(XUIScene.cShell).directChildren(ViewOverlay.cBlackOverlay)), 
				()->{
					css("display:blockb");
					css("display:blockc");
				});
			});

			xStyle(skeyFrame("nom"), ()->{
				from(()-> css("display:blockd") );
				to(()-> css("display:blocke"));
				prct(10, ()-> css("display:blockf"));
			});
		});
		

	}
	
	void doStyle2()
	{
		CSSElement d = xStyle(sSel(cAB, ">", cABC), ()->{
			css("display:blocka");
			css("display:blockb");
			xStyle(sSel("&", cAB, "," ,cABC), ()->{ 
				css("display:blockc");
				css("display:blockd");
				xStyle(sSel("div,h1"), ()->{
					css("display:blocked");
					css("display:blockfd");
				});
			});
			xStyle(sSel("span"), ()->{
				css("display:blocke");
				css("display:blockf");
			});
		});
		
		
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);

		XHTMLFile file = new XHTMLFile();
		XUIFactoryXHtml.ThreadLocalXUIFactoryPage.set(file);
		XMLBuilder buf = new XMLBuilder("css", txtXML, txtXMLAfter);
		d.toXML(buf);
		
		System.out.println(txtXML);
	}
	
	public static void main(String [] args)
	{
		new ScnStyle ().doStyle2();
	}
	

}
