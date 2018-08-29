/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author Bureau
 *
 */

public interface IJSONBuilder {
	
	default <E extends JSClass> E newJava(Class<? extends JSClass> type)
	{
		if (XUIFactory.ThreadLocalXUIFactoryPage.get()==null)
		{
			XHTMLFile file = new XHTMLFile();
			XUIFactory.ThreadLocalXUIFactoryPage.set(file);
		}
		
		return (E)  JSContent.declareType(type, null).asLitteral();
	}
	
	
	default JsonObject obj(Object...value)
	{
		 JsonObjectBuilder jsonObj = Json.createObjectBuilder();
				  
		 for (Object object : value) {
			 
			 if (object instanceof Attr)
			 {
				 Attr attr = (Attr)object;
				 if (attr.value instanceof String)
					 jsonObj.add(attr.key.toString(), attr.value.toString());
				 else if (attr.value instanceof Integer)
					 jsonObj.add(attr.key.toString(), (Integer)attr.value);
				 else if (attr.value instanceof Double)
					 jsonObj.add(attr.key.toString(), (Double)attr.value);
				 else if (attr.value instanceof XMLElement)
				 {
						StringBuilder txtHtml = new StringBuilder(1000);
						StringBuilder txtJS = new StringBuilder(1000);

						((XMLElement)attr.value).toXML(new XMLBuilder("js", txtHtml, txtJS).setModeString(false));
						
						String strHTML =txtHtml.toString();
						strHTML = strHTML.replaceAll("\\n", "");
						strHTML = strHTML.replaceAll("\\t", "");
						String strJS =txtJS.toString();    //StringEscapeUtils.escapeJson(
						strJS = strJS.replaceAll("\\n", "");
						strJS = strJS.replaceAll("\\t", "");
						strJS = strJS.replace("/", "\\/");
						jsonObj.add("html", strHTML);
						jsonObj.add("js", strJS);
				 }
				 else if (attr.value instanceof JsonValue)
					 jsonObj.add(attr.key.toString(), (JsonValue)attr.value);
			 }
			 else
				 CoreLogger.getLogger(2).severe("pb obj");
		 }
		
		return jsonObj.build();
		
	}
	
	default JsonArray arr(Object...items)
	{
		JsonArrayBuilder arr = Json.createArrayBuilder();
		
		for (Object object : items) {
			 if (object instanceof JsonValue)
				 arr.add((JsonValue)object);
			 else if (object instanceof Integer)
				 arr.add((Integer)object);
			 else if (object instanceof String)
				 arr.add((String)object);
			 else
				 CoreLogger.getLogger(2).severe("pb arr");
		}
		return arr.build();
	}
	
	default Object v(Object key, Object value)
	{
		return new Attr(key, value);
	}
	
	static class Attr
	{
		Object key;
		Object value;
		
		public Attr(Object key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		
	}
}
