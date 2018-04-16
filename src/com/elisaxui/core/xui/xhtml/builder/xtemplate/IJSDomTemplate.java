/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IJSDomTemplate extends IXHTMLBuilder {
	XMLElement getTemplate();

	default JSNodeTemplate createDomTemplate(XMLElement xmlElement) {
		return new JSNodeTemplate(xmlElement).setModeJS(true);

	}
	
	/** todo js a mettre dans JSDataBinding */
	default JSFunction vBindable(JSElement row, JSAny value) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		String[] attr = value.toString().split("\\.");		
		fct._return(JSNodeTemplate.MTH_ADD_DATA_BINDING,"(", domItem, ",", row ,",'",  attr[1],"',", value  , ")");
		return fct;
	}
	
	/** todo js a mettre dans JSDataBinding */
	default JSFunction vIf(JSFunction bind, Object elem) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct	.__("var f="+ bind)
			.__("var v=f.call(this, domItem)")
		   ._if("v")
		   		._return(elem)
		   .endif()
		   ; 
		  
		return fct;
	}

}
