/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.value.JSString;
import com.elisaxui.core.xui.xhtml.js.value.JSon;
import com.elisaxui.xui.core.widget.container.JSContainer;

/**
 * @author Bureau
 *
 */
public interface TKActivity extends JSClass {

	public static final String ON_ACTIVITY_CREATE = "onActivityCreate";
	public static final String ON_ACTIVITY_RESUME = "onActivityResume";
	public static final String ON_ACTIVITY_PAUSE = "onActivityPause";
	
	JSContainer jsContainer = null;
	JSon listRegisterActivity = null;
	JSString idCurrentActivity = null;
	TKRouter _tkrouter =null;
	
	default Object constructor()
	{
		set(listRegisterActivity, "{}")
		.set(jsContainer, _new())
		.set(idCurrentActivity, "''")
		;
		return  _void();
	}
	
	default Object getCurrentActivity()
	{
		return jsvar(listRegisterActivity, "[", idCurrentActivity, "]");
	}
	
	default Object getCurrentIDActivity()
	{
		__();
		return idCurrentActivity;
	}
	
	default Object setCurrentActivity(Object id)
	{
	    set(idCurrentActivity, "id");
		return jsvar(listRegisterActivity, "[", idCurrentActivity, "]");
	}
	
	default Object createActivity(Object json)
	{

		var("jsonContainer", jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		.set("json.active", true)
		.__(listRegisterActivity,"[json.id] = json;")
		.set(idCurrentActivity, "json.id")
		;
		return _void();
	}
	
	default Object prepareActivity(Object json)
	{
		var("jsonContainer", jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		.__(listRegisterActivity,"[json.id] = json;")
		;
		return _void();
	}
	
	default Object registerActivity(Object json)
	{
		__(listRegisterActivity,"[json.id] = json;")
		;
		return _void();
	}
	
}
