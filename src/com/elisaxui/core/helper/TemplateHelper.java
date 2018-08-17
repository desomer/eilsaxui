/**
 * 
 */
package com.elisaxui.core.helper;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.platform.commons.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * @author gauth
 *
 */
public class TemplateHelper {

	private static TemplateEngine templateEngine;

	private static TemplateEngine getTemplateEngine() {
		if (null == templateEngine) {
			templateEngine = new TemplateEngine();
			StringTemplateResolver templateResolver = new StringTemplateResolver();
			templateResolver.setTemplateMode(TemplateMode.HTML);
			templateResolver.setCacheable(false);
			templateEngine.setTemplateResolver(templateResolver);
		}
		return templateEngine;
	}

	public static String getTemplateFragment(String htmlContent, Set<String> templateSelectors, Context ctx) {
		templateEngine = getTemplateEngine();
		return templateEngine.process(htmlContent, templateSelectors, ctx);
	}

}
