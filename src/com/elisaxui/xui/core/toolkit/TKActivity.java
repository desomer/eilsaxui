/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;
import com.elisaxui.xui.core.toolkit.json.JActivity;
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
	TKRouterEvent _tkrouter =null;
	
	default Object constructor()
	{
		set(listRegisterActivity, "{}");
		set(jsContainer, _new());
		set(idCurrentActivity, "''");
		return  _void();
	}
	
	default Object getCurrentActivity()
	{
		return listRegisterActivity.attrByString(idCurrentActivity);
	}
	
	default JSString getCurrentIDActivity()
	{
		return idCurrentActivity;
	}
	
	default Object setCurrentActivity(JSString id)
	{
	    set(idCurrentActivity, id);
	    return listRegisterActivity.attrByString(idCurrentActivity);
	}
	
	default Object createActivity(JActivity json)
	{
		JSArray jsonContainer = declareType(JSArray.class, "jsonContainer"); 
		
		var(jsonContainer, jsContainer.getData("'.scene'"));
		set(json.active(), true);
		__(jsonContainer.push(json));
		set(listRegisterActivity.attrByString(json.id()), json);
		set(idCurrentActivity, json.id());
		
		return _void();
	}
	
	default Object prepareActivity(JActivity json)
	{
		JSArray jsonContainer = declareType(JSArray.class, "jsonContainer"); 
		
		var(jsonContainer, jsContainer.getData("'.scene'"));
		set(json.active(), false);
		__(jsonContainer.push(json));
		set(listRegisterActivity.attrByString(json.id()), json);
		
		return _void();
	}
	
//	default Object registerActivity(JActivity json)
//	{
//		set(listRegisterActivity.attrByString(json.id()), json);
//		return _void();
//	}
	
}
