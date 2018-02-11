/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.app.elisys.xui.page.perf.DtoUser;
import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IXHTMLTemplate extends IXHTMLBuilder {
	XMLElement getTemplate();
	

	default XHTMLTemplateImpl xTemplateJS(XMLElement xmlElement)
	{
		return new XHTMLTemplateImpl(xmlElement).setModeJS(true);
		
	}

	default Object xData(JSArray<?> data, Object enter )
	{
		JSon d =  JSClass.declareType(JSon.class, "d");
		
		return new JSFunction().setParam(new Object[] {d})
				.__("JSXHTMLTemplate.doTemplateDataDriven(",d,",",data,","+enter+")")
				._return("e('h2', ['ok'])");
	}

}
