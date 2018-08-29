/**
 * 
 */
package com.elisaxui.app.test;

import com.elisaxui.core.extern.JSMinifier;
import com.elisaxui.core.extern.ThymleafHelper;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */

@xResource(id = "ScnThymVue")
@xComment("Scene ScnThymVue")
public class ViewThymVueJS extends XHTMLPart {

	ThymleafHelper templateMgr = new ThymleafHelper(ViewThymVueJS.class);

	public ViewThymVueJS() {
		templateMgr.getCtx().setVariable("modeapp", false);
		templateMgr.getCtx().setVariable("user", "toto");
	}

	@xTarget(HEADER.class)
	@xResource
	@xPriority(200)
	public XMLElement xImport() {
		return xElem(
				templateMgr.getFragment("ViewVueFile.html::import"),
				templateMgr.getFragment("ViewVueFile.html::css"));
	}

	@xTarget(CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xAppShell() {
		return xElem(templateMgr.getFragment("ViewVueFile.html::appshell"));
	}

	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xVueJS() {
		return  xScriptJS( JSMinifier.optimizeJS(templateMgr.getFragment("ViewVueFile.html::js"), true));
	}

	/****************************************************************************************************/

}
