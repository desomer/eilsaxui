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

//	JSNavBar _jsNavBar = null;
	JSContainer jsContainer = null;
	Object listRegisterActivity = null;
	
	
	default Object constructor()
	{
		set(listRegisterActivity, "{}");
		set(jsContainer, _new());
		return null;
	}
	default Object createActivity(Object json)
	{

		var("jsonContainer", jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		.__(listRegisterActivity,"[json.id] = json;")
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
