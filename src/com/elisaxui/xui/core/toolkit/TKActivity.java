/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.widget.container.JSContainer;
/**
 * @author Bureau
 *
 */
public interface TKActivity extends JSClass {

	JSContainer jsContainer = null;
	Object listRegisterActivity = null;
	Object idCurrentActivity = null;
	
	default Object constructor()
	{
		set(listRegisterActivity, "{}")
		.set(jsContainer, _new())
		.set(idCurrentActivity, "''")
		;
		return null;
	}
	
	default Object getCurrentActivity()
	{
		
		__("return ",listRegisterActivity, "[", idCurrentActivity, "]")
		;
		return null;
	}
	
	
	default Object setCurrentActivity(Object id)
	{
	    set(idCurrentActivity, "id")
	    .__("return ",listRegisterActivity, "[", idCurrentActivity, "]")
	    ;
		return null;
	}
	
	default Object createActivity(Object json)
	{

		var("jsonContainer", jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		.set("json.active", true)
		.__(listRegisterActivity,"[json.id] = json;")
		.set(idCurrentActivity, "json.id")
		;
		return null;
	}
	
	default Object prepareActivity(Object json)
	{

		var("jsonContainer", jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		.__(listRegisterActivity,"[json.id] = json;")
		;
		return null;
	}
	
	default Object registerActivity(Object json)
	{
		__(listRegisterActivity,"[json.id] = json;")
		;
		return null;
	}
	
}
