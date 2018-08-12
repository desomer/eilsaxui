/**
 * 
 */
package com.elisaxui.component.widget.activity;

import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author Bureau
 *
 */
@Deprecated
public interface TKActivity extends JSClass {

	public static final String ON_ACTIVITY_CREATE = "onActivityCreate";
	public static final String ON_ACTIVITY_RESUME = "onActivityResume";
	public static final String ON_ACTIVITY_PAUSE = "onActivityPause";
	
	JSContainer jsContainer = null;
	JSon listRegisterActivity = null;
	JSString idCurrentActivity = null;
	
	default void constructor()
	{
		_set(listRegisterActivity, "{}");
		_set(jsContainer, _new());
		_set(idCurrentActivity, txt());
	}
	
	default JActivity getCurrentActivity()
	{
		return cast(JActivity.class,  listRegisterActivity.attrByStr(idCurrentActivity));
	}
	
	default JSString getCurrentIDActivity()
	{
		return idCurrentActivity;
	}
	
	default JActivity setCurrentActivity(JSString id)
	{
	    _set(idCurrentActivity, id);
	    return cast(JActivity.class,  listRegisterActivity.attrByStr(idCurrentActivity)); 
	}
	
	default JSVoid createActivity(JActivity activity)
	{
		
		JSArray jsonContainer = let(JSArray.class, "jsonContainer", jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		activity.active().set(true);
		jsonContainer.push(activity);
		listRegisterActivity.attrByStr(activity.id()).set(activity);
		idCurrentActivity.set(activity.id());	
		return _void();
	}
	
	default void createActivity2(JActivity activity)    // TODO a faire marcher
	{
		
		JSArray jsonContainer = let(JSArray.class, "jsonContainer", jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		activity.active().set(true);
		jsonContainer.push(activity);
		listRegisterActivity.attrByStr(activity.id()).set(activity);
		idCurrentActivity.set(activity.id());	
	}
	
	default JSVoid prepareActivity(JActivity json)
	{
		JSArray jsonContainer = declareType(JSArray.class, "jsonContainer"); 
		
		_var(jsonContainer, jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		_set(json.active(), false);
		__(jsonContainer.push(json));
		
		_set(listRegisterActivity.attrByStr(json.id()), json);
		
		return _void();
	}
	
//	default Object registerActivity(JActivity json)
//	{
//		set(listRegisterActivity.attrByString(json.id()), json);
//		return _void();
//	}
	
}
