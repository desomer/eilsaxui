/**
 * 
 */
package com.elisaxui.component.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xExport;

/**
 * @author gauth
 *
 */
@xComment("TKPubSub")
@xExport
public interface TKPubSub extends JSClass {

	JSArray<Object> observers();
	
	default void constructor()
	{
		observers().set(JSArray.newLitteral() );
	}
	
	default void subscribe(Object observer)
	{
		observers().push(var(observer));
	}
	
	default void publish(Object message)
	{
		JSInt i = declareType(JSInt.class, "i"); 
		_forIdx(i, observers())._do(() -> {
			__(observers().at(i),"(",  message, ")");
		});
	}
	
}
