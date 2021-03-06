/**
 * 
 */
package com.elisaxui.component.widget.button;

import static com.elisaxui.component.toolkit.transition.CssTransition.*;

import com.elisaxui.component.page.old.XUIScene;

import static com.elisaxui.component.page.CssPage.*;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewFloatAction extends XHTMLPart {


///* Rules for sizing the icon. */
//.material-icons.md-18 { font-size: 18px; }
//.material-icons.md-24 { font-size: 24px; }
//.material-icons.md-36 { font-size: 36px; }
//.material-icons.md-48 { font-size: 48px; }
//
///* Rules for using icons as black on a light background. */
//.material-icons.md-dark { color: rgba(0, 0, 0, 0.54); }
//.material-icons.md-dark.md-inactive { color: rgba(0, 0, 0, 0.26); }
//
///* Rules for using icons as white on a dark background. */
//.material-icons.md-light { color: rgba(255, 255, 255, 1); }
//.material-icons.md-light.md-inactive { color: rgba(255, 255, 255, 0.3); }


	static CSSClass cFloatAction;
	
	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {

		return cStyle().path(cFloatAction)
						.set(""
							//+ "z-index:"+XUIScene.ZINDEX_FLOAT+";"
							+ " position: fixed; "
							+ "left: calc("+widthScene+" - "+ViewBtnCircle.SIZE_CIRCLE+" - 1rem); "
							+ "top: calc(100vh - "+ViewBtnCircle.SIZE_CIRCLE+" - "+heightTabBar+" - 1rem); "
								)
		;
	}
	
	@xTarget(CONTENT.class)
	public static XMLElement getTemplateBtnFloat() {
		
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xDiv(cFloatAction, cFixedElement, xIdAction("\"BtnFloatMain\""),	vPart(new ViewBtnCircle().vProperty(ViewBtnCircle.PROPERTY_ICON, "history")));
			}
		};
		
		return template.getTemplate();
	}
	
	public static XMLElement getTemplate() {
		return vPart(new ViewFloatAction());
	}
	
}
