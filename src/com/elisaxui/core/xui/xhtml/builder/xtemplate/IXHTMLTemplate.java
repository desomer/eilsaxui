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
	

	default XHTMLTemplateImpl jsTemplate(XMLElement xmlElement)
	{
		return new XHTMLTemplateImpl(xmlElement).setModeJS(true);
		
	}

	default JSFunction xDataDriven(JSArray<?> data, Object enter, Object exit )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSXHTMLTemplate.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+", null)")
				;
	}
	
	default JSFunction xDataDriven(JSArray<?> data, Object enter, Object exit, Object change )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSXHTMLTemplate.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+","+change+")")
				;
	}

}
