/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface JSTouchManager extends JSClass {
	
	JSInt idx = JSClass.declareType();
	JSEventTouch aEvent = JSClass.declareType();
	JSArray<JSString> listEvent = JSClass.declareType();
	TTouchInfo aTouchInfo = JSClass.declareType();
	
	TTouchInfo touchInfo();  // variable de class (ici static)
		
	@xStatic(autoCall = true) // appel automatique de la methode static
	default void main() {
		
		let(aTouchInfo, newJS(TTouchInfo.class));
		
		touchInfo().set(aTouchInfo);
		
		JSArray<JSString> listEventLitteral = JSArray.newLitteral();
		listEventLitteral.push(JSString.value("touchstart"));
		listEventLitteral.push(JSString.value("touchend"));
		listEventLitteral.push(JSString.value("touchcancel"));
		listEventLitteral.push(JSString.value("touchleave"));
		listEventLitteral.push(JSString.value("touchmove"));
		
		let(listEvent, listEventLitteral);
		
		_forIdx(idx, listEvent)._do(() -> {
			document().addEventListener(listEvent.at(idx), fct(aEvent, () -> {
				_if(aEvent.type().equalsJS("touchstart")).then(() -> {
					aTouchInfo.startTime().set(aEvent.timeStamp());
					aTouchInfo.stopTime().set(0);
					aTouchInfo.deltaTime().set(0);
					aTouchInfo.startX().set(aEvent.touches().at(0).pageX());
					aTouchInfo.startY().set(aEvent.touches().at(0).pageY());
					aTouchInfo.startClientX().set(aEvent.touches().at(0).clientX());
					aTouchInfo.startClientY().set(aEvent.touches().at(0).clientY());
					
					callStatic(JSActionManager.class).searchStart(aTouchInfo, aEvent);
					
				})._elseif(aEvent.type().equalsJS("touchmove")).then(() -> {
					aTouchInfo.deltaTime().set(aEvent.timeStamp().substact(aTouchInfo.startTime()));
					
					aTouchInfo.deltaX().set(aEvent.touches().at(0).clientX().substact(aTouchInfo.startClientX()));
					aTouchInfo.deltaY().set(aEvent.touches().at(0).clientY().substact(aTouchInfo.startClientY()));
					
					callStatic(JSActionManager.class).searchMove(aTouchInfo, aEvent);
					
				})._else(()->{
					aTouchInfo.stopTime().set(aEvent.timeStamp());
					aTouchInfo.deltaTime().set(aTouchInfo.stopTime().substact(aTouchInfo.startTime()));
					aTouchInfo.stopX().set(aEvent.changedTouches().at(0).pageX());
					aTouchInfo.stopY().set(aEvent.changedTouches().at(0).pageY());
					
					aTouchInfo.deltaX().set(aTouchInfo.stopX().substact(aTouchInfo.startX()));
					aTouchInfo.deltaY().set(aTouchInfo.stopY().substact(aTouchInfo.startY()));
					
					aTouchInfo.distanceX().set("Math.abs(",aTouchInfo.stopX().substact(aTouchInfo.startX()), ")");
					aTouchInfo.distanceY().set("Math.abs(",aTouchInfo.stopY().substact(aTouchInfo.startY()), ")");
					
					callStatic(JSActionManager.class).searchStop(aTouchInfo, aEvent);
				});
				
			}));
		});
	}
	
	public interface TTouchInfo extends JSType
	{
		JSInt startTime();
		JSInt stopTime();
		JSInt deltaTime();
		
		JSInt startX();
		JSInt startY();
		
		JSInt startClientX();
		JSInt startClientY();
		
		JSInt stopX();
		JSInt stopY();
		
		JSInt deltaX();
		JSInt deltaY();
		
		JSInt distance();
		JSInt velocity();
		
		JSInt distanceX();
		JSInt velocityX();
		
		JSInt distanceY();
		JSInt velocityY();
		
       // float distance = Math.sqrt((newX-oldX) * (newX-oldX) + (newY-oldY) * (newY-oldY));
      //  float speed = distance / timerTime;
	}
}
