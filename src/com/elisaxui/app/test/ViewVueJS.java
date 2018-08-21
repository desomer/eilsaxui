/**
 * 
 */
package com.elisaxui.app.test;

import com.elisaxui.core.extern.TemplateHelper;
import com.elisaxui.core.helper.URLLoader;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xExtend;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
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
public class ViewVueJS extends XHTMLPart {
	
	TemplateHelper templateMgr = new TemplateHelper();
	
	public ViewVueJS()
	{
		templateMgr.addTemplate("main", URLLoader.loadResourceNearClass(ViewVueJS.class, "ViewVueFile.html", false));
	}
	
	@xTarget(HEADER.class)
	@xResource
	@xPriority(200)
	public XMLElement xImport() {
		return xElem( 
				templateMgr.getFragment("main::import") , 
				templateMgr.getFragment("main::css"));
	}
	
	@xTarget(CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xAppShell() {
		return xElem(templateMgr.getFragment("main::appshell"));
	}
	
	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xVueJS() {
		return xElem(getTemplateVue());
	}
	
	
	public String getTemplateVue() {
		String js = templateMgr.getFragment("main", "js", null);
		js = templateMgr.getBeautifyJS(js, false);
		return js;
	}

	/****************************************************************************************************/
	
	@xTarget(AFTER_CONTENT.class)
	@xResource
	@xPriority(200)
	public XMLElement xAddJS() {
		return xElem(JSVue.class);
	}
	
	@xCoreVersion("1")
	@xExtend(file = "JSVue.js")
	public interface JSVue extends JSClass {

		@xStatic(autoCall = true)
		default void init() {
			__(getTemplateTLF(this));
		}
	}
	
	
	private static final String CODE_EXT = "code.ext";
	
	public static String getTemplateTLF(Object instance) {

		TemplateHelper templateManager = new TemplateHelper();
		templateManager.getCtx().setVariable("align", "right");
		templateManager.getCtx().setVariable("date", "11/03/1973");
		templateManager.getCtx().setVariable("admin", false);
		templateManager.getCtx().setVariable("version", false);
		templateManager.addTemplate("main", URLLoader.loadResourceNearClass(ViewVueJS.class, "ViewVueFileSimple.html", false));
		templateManager.addTemplate(CODE_EXT, "console.debug('[[${date}]]')");
		
		String css = templateManager.getFragment("main", "css", "style");
		String template = templateManager.getFragment("main", "template", "template");
		
		String templateJS = templateManager.getHtmlToJS(template);
		String cssJS =templateManager. getHtmlToJS(css);
		
		templateManager.getCtx().setVariable("renderer", templateJS);
		templateManager.getCtx().setVariable("stylejs", cssJS);
		
		String js = templateManager.getFragment("main", "js", "script");

		js = templateManager.getBeautifyJS(js, false);

		return js;
	}
}
