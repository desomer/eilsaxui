/**
 * 
 */
package com.elisaxui.component.widget.loader;

import static com.elisaxui.component.widget.button.ViewRippleEffect.cRippleEffect;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewLoader extends XHTMLPart {

	
	static CSSClass cLoaderContainer;
	static CSSClass cLoaderLoader;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		
		return xStyle().path(cLoaderContainer) .set(" position:absolute;left:50%; width:50vw; height:50vw;  top:50%; transform: translate(-50%,-50%);")
					 .path(cLoaderLoader) .set(" transition: all 0.7s ease-in-out;"
					 		+ "border:10px solid #ebebeb; border-bottom-color:"+((XUIScene)XUIFactoryXHtml.getXHTMLFile().getScene()).getConfigScene().getBgColorTheme()+";"
					 		+ "width:100%;height:100%;border-radius:50%;"
					 		+ "box-shadow: 0px 13px 20px 0px #fd42a430;"
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
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xDiv(cLoaderContainer, xDiv(cLoaderLoader));
			}
		};
		
		return template.getTemplate();
	}
	
	
	
}
