/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.util.ArrayList;

import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;


/**
 * @author Bureau
 *
 */
public class JSClassMethod extends JSVariable {

	ArrayList<Object> listContent = new ArrayList<Object>();

	
	public void reinit()
	{
		listContent.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		
		for (Object object : listContent) {
			sb.append(object);
		}
		
		return sb.toString();
	}
	
	
	public <E extends JSClassMethod> E addContent(Object content) {
		
		if ( content instanceof Object[])
		{
			Object[] arr = (Object[])content;
			for (Object object : arr) {
				
				if (object instanceof CSSClass)
					listContent.add(((CSSClass)object).getId());
				else if ( object instanceof Object[])
				{
					addContent(object);
				}
				else
					listContent.add(object);
			}
		}
		else if (content!=null)
			listContent.add(content);
		return (E)this;
	}
	
	public <E extends JSClassMethod> E  callMth(String mth, Object...classes)
	{
		E ret = (E)this;
		if (listContent.size()==0 && this.name!=null)
		{
			// gestion premier appel de variable
			try {
				ret = (E) this.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ret.setName(this.getName());
		}
		
		ret.addContent("."+mth+"(");
		ret.addContent(classes);
		ret.addContent(")");
		return ret;
	}
}
