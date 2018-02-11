/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
public interface JSXHTMLTemplate extends JSClass {

	@xStatic
	default void doTemplateDataDriven(JSon parent, JSArray<?> data, JSVariable fctEnter )
	{
		JSDataSet aDataSet = let("aDataSet", newInst(JSDataSet.class) );
		aDataSet.setData(data);
	
		JSDataDriven aDataDriven = let("aDataDriven", newInst(JSDataDriven.class, aDataSet) );
		aDataDriven.onEnter(fct("ctx").__(()->{
			__("ctx.parent = parent");
			__(parent, ".appendChild(",  fctEnter, ".call(this, ctx.row, ctx))");
		}));
	}
	
}
