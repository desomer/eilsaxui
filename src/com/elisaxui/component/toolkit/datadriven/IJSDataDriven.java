/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSNodeTemplate;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IJSDataDriven  {
	

	/******************************************************************************/
	
	default JSFunction xDataDriven(JSElement data, JSElement enter )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().setComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+",null, null)")
				;
	}
	
	
	default JSFunction xDataDriven(JSElement data, JSElement enter, JSElement exit )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().setComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+", null)")
				;
	}
	
	default JSFunction xDataDriven(JSElement data, JSElement enter, JSElement exit, JSElement change )
	{
		JSon domparent =  JSContent.declareType(JSon.class, "domparent");
		
		return (JSFunction) new JSFunction().setComment("xDataDriven").setParam(new Object[] {domparent})
				.__("JSDataDriven.doTemplateDataDriven(",domparent,",",data,","+enter+","+exit+","+change+")")
				;
	}
	
	public default JSFunction onEnter(Object row, XMLElement elem)
	{
		return (JSFunction) new JSFunction().setComment("onEnter").setParam(new Object[] {row, "ctx"})
				._return( new JSNodeTemplate(elem).setModeJS(true) );

	}
	
	public default JSFunction onEnter(Object row, Object elem)
	{
		return (JSFunction) new JSFunction().setComment("onEnter").setParam(new Object[] {row, "ctx"})
				._return( elem );

	}
	
	public default JSFunction onExit(Object row, Object dom)
	{
		return (JSFunction) new JSFunction().setComment("onExit").setParam(new Object[] {row, dom, "ctx"})
				._if(dom,"!=null")
					.__("$("+dom+").remove()")
				.endif();
	}
	
	public default JSFunction onChange(JSChangeCtx ctx, JSNodeElement aDom, JSFunction action )
	{
		return (JSFunction) new JSFunction().setComment("onChange").setParam(new Object[] {"ctx"})
				._var("dom" , "ctx.row['"+JSDataSet.ATTR_DOM_LINK+"']")
				._var("f", action.setParam(new Object[] {ctx, aDom}))
				.__("f(ctx, dom)")
				;		
	}
	
}
