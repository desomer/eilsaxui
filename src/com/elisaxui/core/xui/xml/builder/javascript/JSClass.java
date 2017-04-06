package com.elisaxui.core.xui.xml.builder.javascript;

import com.elisaxui.core.xui.xhtml.XHTMLPart;

public interface JSClass extends JSInterface  {

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
