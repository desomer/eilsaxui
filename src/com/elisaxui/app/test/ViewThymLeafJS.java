/**
 * 
 */
package com.elisaxui.app.test;

import com.elisaxui.core.extern.JSMinifier;
import com.elisaxui.core.extern.ThymleafHelper;
import com.elisaxui.core.helper.URLLoader;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;

/**
 * @author gauth
 *
 */

@xResource(id = "ScnThym")
@xComment("Scene ScnThym")
public class ViewThymLeafJS extends XHTMLPart {

	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xVueJS() {
		return  xScriptJS( getTemplateTLF() );
	}

	/****************************************************************************************************/

	public String getTemplateTLF() {

		ThymleafHelper templateManager = new ThymleafHelper(ViewThymLeafJS.class);
		templateManager.getCtx().setVariable("align", "right");
		templateManager.getCtx().setVariable("date", "11/03/1973");
		templateManager.getCtx().setVariable("admin", false);
		templateManager.getCtx().setVariable("version", false);
		
		String mainFrag = URLLoader.loadResourceNearClass(ViewThymLeafJS.class, "ViewVueFileSimple.html", false);
		templateManager.addTemplate("main", mainFrag);
		templateManager.addTemplate("code", "console.debug('[[${date}]]')");

		String css = templateManager.getFragment("main::css");
		String template = templateManager.getFragment("main::template");

		String templateJS = templateManager.getHtmlToJS(template);
		String cssJS = templateManager.getHtmlToJS(css);

		templateManager.getCtx().setVariable("renderer", templateJS);
		templateManager.getCtx().setVariable("stylejs", cssJS);

		return templateManager.getFragment("main::js");
	}
}
