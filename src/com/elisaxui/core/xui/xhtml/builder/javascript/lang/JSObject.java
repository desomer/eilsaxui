/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ILitteral;

/**
 * @author gauth
 *
 */
public class JSObject extends JSAny  implements ILitteral, IJSClassInterface{
	public JsonObjectBuilder jsonBuilder = null;
	
	@Override
	public String zzGetJSClassType() {
		return "Object";
	}
	
	public static JSObject newLitteral()
	{
		return new JSObject().asLitteral();
	}
	
	public <E> E asLitteral()
	{
		jsonBuilder = Json.createObjectBuilder();
		return (E) this;
	}
	
	@Override
	public boolean isLitteral() {
		return jsonBuilder!=null;
	}
	
	@Override
	public Object _getValue() {
		if (jsonBuilder!=null)
			return jsonBuilder.build().toString();
		else
			return super._getValue();
	}
	
	@Override
	public final <E extends JSAny> E attr(String att) {
		if (isLitteral())
		{
			JSAny ret = declareTypeAny();
			ret._setName(att);
			ret._setParentLitteral(this);
			return (E) ret;
		}
		else
			return super.attr(att);
	}
	
	@Override
	public String getStringJSON() {
		return ""+_getValue();
	}
}
