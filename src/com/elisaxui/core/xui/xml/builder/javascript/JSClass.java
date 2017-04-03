package com.elisaxui.core.xui.xml.builder.javascript;

public interface JSClass  {
	
	JSContent js();
	
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
