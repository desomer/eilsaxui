/**
 * 
 */
package com.elisaxui.component.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xComment("TKPubSub")
@xExport
@xCoreVersion("1")
public interface TKPubSub extends JSClass {

	JSInt i = JSClass.declareType();

	JSArray<JSCallBack> observers();

	default void constructor() {
		observers().set(JSArray.newLitteral());
	}

	default void subscribe(Object observer) {
		observers().push(cast(JSCallBack.class, observer));
	}
	
	default JSInt unsubscribe(Object observer) {
		let(i, observers().indexOf(cast(JSCallBack.class, observer)));
		_if(i, ">-1").then(() -> {
			observers().splice( i, 1 );
		});
		return i;
	}

	default void publish(Object message) {
		_forIdx(i, observers())._do(() -> observers().at(i).invoke(var(message)));
	}

}
