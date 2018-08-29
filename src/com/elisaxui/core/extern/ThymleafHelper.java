/**
 * 
 */
package com.elisaxui.core.extern;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptException;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import com.elisaxui.app.test.ViewThymLeafJS;
import com.elisaxui.core.helper.URLLoader;
import com.elisaxui.core.helper.log.CoreLogger;

/**
 * @author gauth
 * 
 *         https://stackoverflow.com/questions/43674862/thymeleaf-script-and-thblock
 *         https://stackoverflow.com/questions/30616976/conditional-text-in-thymeleaf-how-to-do-it-in-plain-text
 *         https://github.com/thymeleaf/thymeleaf/issues/394
 *         https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#fragment-specification-syntax
 *         https://blog.zenika.com/2013/01/18/introducing-the-thymeleaf-template-engine/
 *         https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html
 */
public class ThymleafHelper {

	private TemplateEngine templateEngine;
	HashMap<String, String> mapTemplate;
	Context ctx;
	Object near;

	/**
	 * @return the ctx
	 */
	public final Context getCtx() {
		return ctx;
	}

	/**
	 * @param ctx the ctx to set
	 */
	public final void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * @param mapTemplate
	 */
	public ThymleafHelper(Object near) {
		super();
		this.mapTemplate = new HashMap<>();
		ctx = new Context();
		ctx.setVariable("templateMgr", this);
		this.near = near;
	}

	public ThymleafHelper addTemplate(String name, String template) {
		mapTemplate.put(name, template);
		return this;
	}

	private TemplateEngine getTemplateEngine() {
		if (null == templateEngine) {
			templateEngine = new TemplateEngine();

			MyThymeleafViewResolver mtlvr = new MyThymeleafViewResolver(near, mapTemplate);
			mtlvr.setCacheable(true);
			mtlvr.setCacheTTLMs(TimeUnit.SECONDS.toMillis(1));
			mtlvr.setOrder(1);
			templateEngine.addTemplateResolver(mtlvr);
			templateEngine.addDialect(new MyExpression());

		}
		return templateEngine;
	}

	private String getTemplateFragment(String htmlContent, Set<String> templateSelectors, Context ctx) {
		templateEngine = getTemplateEngine();
		return templateEngine.process(htmlContent, templateSelectors, ctx);
	}

	/**
	 * @param js
	 * @return
	 */
	public String getBeautifyJS(String js, boolean optimize) {
		js = js.replace("\r", "");
		js = js.replace("\n\t\n\t", "\n\t");
		js = js.replace("\t\n\n", "\n");
		if (optimize)
			js = JSMinifier.optimizeJS(js, true);
		return js;
	}

	/**
	 * @param template
	 * @return
	 */
	public String getHtmlToJS(String template) {
		String[] lines = template.replace("\r\n", "\n").replace("\r", "\n").replace("'", "\"").split("\n", -1);
		StringBuilder templateStr = new StringBuilder("[\n");
		int nb = 0;
		for (int i = 0; i < lines.length; i++) {
			if (!("".equals(lines[i]))) {
				if (nb > 0)
					templateStr.append(",\n");
				templateStr.append("\t\t'" + lines[i] + "'");
				nb++;
			}
		}
		templateStr.append("\n\t\t].join('')");
		return templateStr.toString();
	}

	public String getFragment(String fragment) {
		String[] listTag = fragment.trim().split("::");
		return getFragment(listTag[0],listTag[1]);
		
	}
	
	/**
	 * @param loadResource
	 * @param ctx
	 * @return
	 */
	public String getFragment(String loadResource, String idFragment) {
		final Set<String> fragmentsTemplate = new HashSet<>();
		fragmentsTemplate.add(idFragment);
		String fragment = getTemplateFragment(loadResource, fragmentsTemplate, ctx);
		return fragment;
	}

	/*******************************************************************************/
	static class VueJS
	{
		/**
		 * @param context
		 * @param manager
		 */
		public VueJS(IExpressionContext context) {
			super();
			this.context = context;
		}

		IExpressionContext context;
		
		public Object compile(String templateId)
		{
			String[] listTag = templateId.trim().split("::");
			
			ThymleafHelper mgr = (ThymleafHelper) context.getVariable("templateMgr");
			String template = mgr.getFragment(listTag[0], listTag[1]);
			
			String templateJS = null;
			try {
				JSExeHelper.init();
				JSExeHelper compilMgr = new JSExeHelper();
				templateJS = compilMgr.compileVueJs(template);
			} catch (ScriptException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			
			return templateJS;
		}
	}
	
	/*****************************************************************************/
	static class MyExpression implements IExpressionObjectDialect 
	{

		@Override
		public String getName() {
			return "MyExpression";
		}

		@Override
		public IExpressionObjectFactory getExpressionObjectFactory() {
			return new IExpressionObjectFactory() {

				@Override
				public Set<String> getAllExpressionObjectNames() {
					 return Collections.unmodifiableSet(new LinkedHashSet<String>(java.util.Arrays.asList(
			                    new String[]{ "vuejs" })));
				}

				@Override
				public Object buildObject(IExpressionContext context, String expressionObjectName) {
					
					if ("vuejs".equals(expressionObjectName)) {					
						return new VueJS(context);
					}
					return null;
				}

				@Override
				public boolean isCacheable(String expressionObjectName) {
					return false;
				}};
		}
		
	}
	
	/********************************************************************/
	
	static class MyThymeleafViewResolver extends AbstractConfigurableTemplateResolver {
		Map<String, String> mapTemplate;
		Object nearObject = null;

		public MyThymeleafViewResolver(Object nearObject, Map<String, String> mapTemplate) {
			this.mapTemplate = mapTemplate;
			this.nearObject = nearObject;
		}

		@Override
		protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate,
				String template, String resourceName, String characterEncoding,
				Map<String, Object> templateResolutionAttributes) {
			
			String templateString = mapTemplate.get(resourceName);
			
			CoreLogger.getLogger(1).info(()->"load " + resourceName);
			
			if (templateString==null)
				templateString  = URLLoader.loadResourceNearClass(nearObject, resourceName, false);

			return new StringTemplateResource(templateString);
		}
	}

}
