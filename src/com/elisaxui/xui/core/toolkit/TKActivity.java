/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.widget.activity.JActivity;
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
	
	default JSVoid constructor()
	{
		set(listRegisterActivity, "{}");
		set(jsContainer, _new());
		set(idCurrentActivity, txt());
		return  _void();
	}
	
	default JActivity getCurrentActivity()
	{
		return cast(JActivity.class,  listRegisterActivity.attrByString(idCurrentActivity));
	}
	
	default JSString getCurrentIDActivity()
	{
		return idCurrentActivity;
	}
	
	default JActivity setCurrentActivity(JSString id)
	{
	    set(idCurrentActivity, id);
	    return cast(JActivity.class,  listRegisterActivity.attrByString(idCurrentActivity)); 
	  //  idCurrentActivity.set(id); 
	  //  return getCurrentActivity();   // TODO a faire marcher
	}
	
	default JSVoid createActivity(JActivity activity)
	{
		
		JSArray jsonContainer = let(JSArray.class, "jsonContainer", jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		activity.active().set(true);
		jsonContainer.push(activity);
		listRegisterActivity.attrByString(activity.id()).set(activity);
		idCurrentActivity.set(activity.id());	
		return _void();
	}
	
	default void createActivity2(JActivity activity)    // TODO a faire marcher
	{
		
		JSArray jsonContainer = let(JSArray.class, "jsonContainer", jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		activity.active().set(true);
		jsonContainer.push(activity);
		listRegisterActivity.attrByString(activity.id()).set(activity);
		idCurrentActivity.set(activity.id());	
	}
	
	default JSVoid prepareActivity(JActivity json)
	{
		JSArray jsonContainer = declareType(JSArray.class, "jsonContainer"); 
		
		var(jsonContainer, jsContainer.getData(txt(CSSSelector.onPath(XUIScene.scene))));
		set(json.active(), false);
		__(jsonContainer.push(json));
		
//		createActivity2(json);    // TODO a faire marcher
		
		set(listRegisterActivity.attrByString(json.id()), json);
		
		return _void();
	}
	
//	default Object registerActivity(JActivity json)
//	{
//		set(listRegisterActivity.attrByString(json.id()), json);
//		return _void();
//	}
	
}
