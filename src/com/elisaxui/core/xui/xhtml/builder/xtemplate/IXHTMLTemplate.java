/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
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

	default Object xDataDriven(JSArray<?> data, Object enter, Object exit )
	{
		JSon d =  JSContent.declareType(JSon.class, "d");
		
		return new JSFunction().setParam(new Object[] {d})
				.__("JSXHTMLTemplate.doTemplateDataDriven(",d,",",data,","+enter+","+exit+")")
				;
	}

}
