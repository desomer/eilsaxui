/**
 * 
 */
package com.elisaxui.xui.core.widget.loader;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;

/**
 * @author Bureau
 *
 */
public class ViewLoader extends XHTMLPart {

	
	static XClass cLoaderContainer;
	static XClass cLoaderLoader;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		
		return xCss().select(cLoaderContainer) .set(" position:absolute;left:50%; width:50vw; height:50vw;  top:50%; transform: translate(-50%,-50%);")
					 .select(cLoaderLoader) .set(" transition: all 0.7s ease-in-out;"
					 		+ "border:10px solid #ebebeb; border-bottom-color:"+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorTheme()+";"
					 		+ "width:100%;height:100%;border-radius:50%;"
					 		//+ "-webkit-font-smoothing: antialiased !important; margin:30px 0px;"
					 		+ "-webkit-animation: spin2 3s ease-in-out infinite;")
					 .on("@keyframes spin2", 
								"0% {transform: rotate(0deg);}"
								+"50% { transform: rotate(1020deg)} "
								+"100% { transform: rotate(720deg);  }")
		;
	}
	
	@xTarget(CONTENT.class)
	public static XMLElement getTemplateBtnFloat() {
		return xDiv(cLoaderContainer, xDiv(cLoaderLoader));
	}
	
	
	
}
