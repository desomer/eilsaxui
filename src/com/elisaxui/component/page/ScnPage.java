/**
 * 
 */
package com.elisaxui.component.page;

import com.elisaxui.app.core.module.CmpModuleComponent;
import com.elisaxui.app.core.module.CmpModuleCore;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.widget.button.CssRippleEffect;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */

@xComment("Scene Page")
public class ScnPage extends XHTMLPart implements ICSSBuilder {

	private static CSSClass cMain;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImport() {
		return xElem(new CmpModuleCore(), new CmpModuleComponent());
	}

	@xTarget(HEADER.class)
	@xResource
	public XMLElement preLoad() {
		return xElem(xLinkModulePreload("/asset/mjs/xMount.mjs"),
				xLinkModulePreload("/asset/mjs/xStandard.mjs"),
				xLinkModulePreload("/asset/mjs/xComponent.mjs"),
				xLinkModulePreload("/asset/mjs/xDatadriven.mjs"),
				xLinkModulePreload("/asset/mjs/xBinding.mjs"),
				xLinkModulePreload("/asset/mjs/xCore.mjs"));
	}

	/************************************************************************/

	@xTarget(HEADER.class) // la vue App Shell
	@xResource()
	@xPriority(10)
	public XMLElement sStyle() {
		return xElem(
				new CssReset(),
				new CssRippleEffect(),
				new CssTransition(),
				xStyle(() -> {
					sOn(sSel("html"), () -> css("font-size: 16px;"));
					sOn(sSel("body"), () -> css("font-family: 'Roboto', sans-serif; font-weight: normal;"));
					sOn(sSel(cMain), () -> css("background-color: #333333; min-height: 100vh;"));
				}));
	}

	/**
	 * @return
	 */
	protected XMLElement getAppShell() {
		return xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIdPage, "Appshell"));
	}

	/**
	 * @return the cMain
	 */
	public static CSSClass getcMain() {
		return cMain;
	}

}
