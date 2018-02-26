/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * @author gauth
 *
 */
public class JSObject extends JSClassInterface {
	public JsonObjectBuilder jsonBuilder = null;
	
	@Override
	public String zzGetJSClassType() {
		return "Object";
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
	
}
