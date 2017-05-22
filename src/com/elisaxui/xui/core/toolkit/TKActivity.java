/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;	

/**
 * @author Bureau
 *
 */
public interface TKActivity extends JSClass {

	JSNavBar _jsNavBar = null;
	JSContainer _jsContainer = null;
	
	default Object createActivity(Object json)
	{

		var(_jsContainer, _new())
		.var("jsonContainer", _jsContainer.getData("'.scene'"))
		.__("jsonContainer.push(json)")
		
		;
		return null;
	}
	
}
