/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.Method;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;


/**
 * @author Bureau
 *
 * class JS d'interface vers un JS externe sans implementation interne
 */
public class JSClassInterface extends JSVariable {

	// TODO a retirer et a gerer par le _setContent    (voir equal sur JSString)
	Array<Object> listContent = new Array<Object>();

	protected void reinit()
	{
		listContent.clear();
	}
	
	protected Object[] addText(Object...classes)
	{
		
		
		if (classes.length==1)
		{
			Object[] ret =  new Object[3];
			if (classes[0] instanceof JSVariable)
			{
				return new Object[] { classes[0] };
			}
			if (classes[0] instanceof Integer)
			{
				return new Object[] { classes[0] };
			}	
			else
			{
				ret[0]="'";
				ret[1]=classes[0];
				ret[2]="'";
			}
			return ret;
		}
		

		return new Object[] {"... bug addtext..."};
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		
		for (Object object : listContent) {
			sb.append(object);
		}
		
		return sb.toString();
	}
	
	@Override
	protected Object _getString() {
		Array<Object> list = new Array<Object>();
		Object arr = super._getString();
		if (arr instanceof Array )
			list.addAll((Array<?>)arr);
		else
			list.add(arr);
		
		list.addAll(listContent);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected <E extends JSClassInterface> E addContent(Object content) {
		
		if ( content instanceof Object[])
		{
			Object[] arr = (Object[])content;
			for (Object object : arr) {
				
				if (object instanceof XClass)
					listContent.add(((XClass)object).getId());
				
				else if (object instanceof JSFunction)
				{
					StringBuilder strBuf = new StringBuilder();
					XMLBuilder buf = new XMLBuilder("????", strBuf, null);
					((JSFunction)object).toXML(buf);
					listContent.add(strBuf.toString());
				}
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
	
	@SuppressWarnings("unchecked")
	protected <E extends JSClassInterface> E  callMth(String mth, Object...classes)
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
			ret._setName(this._getName());
		}
		
		ret.addContent("."+mth+"(");
		ret.addContent(classes);
		ret.addContent(")");
		
		registerMethod(ret);
		
		return ret;
	}
	
	
	/**********************************************************************/
	
	
	/**********************************************************************/
    public <E extends JSClassInterface> E attrByString(Object attr)
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
			ret._setName(this._getName());
		}
   	 
   	 ret.addContent("[");
   	 ret.addContent(addText(attr));
   	 ret.addContent("]");
   	 return ret; 
    }
	
    @Deprecated
	public <E extends JSClassInterface> E  attr(String att)
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
			ret._setName(this._getName());
		}
		
		ret.addContent("."+att);
		return ret;
	}
	
	public <E extends JSVariable> E  castAttr(JSVariable cl, String att)
	{		
		cl._setContent(this._getName()+"."+att);
		return (E) cl;
	}
}
