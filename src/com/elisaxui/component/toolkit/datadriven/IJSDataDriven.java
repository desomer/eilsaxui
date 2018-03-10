/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLTemplate;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IJSDataDriven {
	
	default JSFunction vFor(JSElement data, JSElement aRow,  XMLElement elem )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		JSFunction enter = onEnter(aRow, elem);
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+",null, null)")
				;
	}
	
	default JSFunction xDataDriven(JSElement data, JSElement enter )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+",null, null)")
				;
	}
	
	
	default JSFunction xDataDriven(JSElement data, JSElement enter, JSElement exit )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+", null)")
				;
	}
	
	default JSFunction xDataDriven(JSElement data, JSElement enter, JSElement exit, JSElement change )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().zzSetComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+","+change+")")
				;
	}
	
	public default JSFunction onEnter(Object row, XMLElement elem)
	{
		return (JSFunction) new JSFunction().setParam(new Object[] {row, "ctx"})
				._return( new XHTMLTemplate(elem).setModeJS(true) );

	}
	
	public default JSFunction onEnter(Object row, JSDomElement elem)
	{
		return (JSFunction) new JSFunction().zzSetComment("onEnter").setParam(new Object[] {row, "ctx"})
				._return( elem );

	}
	
	public default JSFunction onExit(Object row, Object dom)
	{
		return (JSFunction) new JSFunction().zzSetComment("onExit").setParam(new Object[] {row, dom, "ctx"})
				._if(dom,"!=null")
					.__("$("+dom+").remove()")
				.endif();
	}
	
	public default JSFunction onChange(JSChangeCtx ctx, JSDomElement aDom, JSFunction action )
	{
		return (JSFunction) new JSFunction().zzSetComment("onChange").setParam(new Object[] {"ctx"})
				._var("dom" , "ctx.row['"+JSDataSet.ATTR_DOM_LINK+"']")
				._var("f", action.setParam(new Object[] {ctx, aDom}))
				.__("f(ctx, dom)")
				;		
	}
	
}