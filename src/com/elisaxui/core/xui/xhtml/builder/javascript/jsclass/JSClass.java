package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;

/**
 * interface proxy de class JS
 * @author Bureau
 *
 */
public interface JSClass extends JSMethodInterface  {

	@Deprecated
	static <E extends JSClass> E initVar(Class<E> cl) {
		return XHTMLPart.jsBuilder.getProxy(cl);
	}
	
	
	static Object _new(Class<? extends JSClass> cl, Object...param ) {
		StringBuilder buf = new StringBuilder();
		buf.append("new "+cl.getSimpleName() +"(");
		if (param!=null)
		{
			int i=0;
			for (Object object : param) {
				if (i>0)
					buf.append(", ");
				buf.append(object);
				i++;
			}
		}
		buf.append(")");
		return buf.toString() ;
	}
	
	
}
