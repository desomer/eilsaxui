/**
 * 
 */
package com.elisaxui.component.widget.button;

import static com.elisaxui.component.toolkit.transition.ConstTransition.NEXT_FRAME;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_RIPPLE_EFFECT;

import com.elisaxui.app.core.admin.JSActionManager;
import com.elisaxui.app.core.admin.JSActionManager.TActionEvent;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.FILE;
/**
 * @author Bureau
 *
 */
public class CssRippleEffect extends XHTMLPart {

	
	public static CSSClass cRippleEffect;
	public static CSSClass cRippleEffectShow;
	public static CSSClass cRippleEffectColorBack;

	@xTarget(AFTER_BODY.class)
	@xResource(id = "anim_after.css")
	public XMLElement xStylePart() {

		return cStyle()
				.on(cRippleEffect, "overflow: hidden; ") 
				.on(CSSSelector.onPath(cRippleEffect, ":after"), " content: ''; "
						+ "  position: absolute; top: 50%;  left: 50%;  width: 5px;  height: 5px;"
						+ "  background: rgba(255, 255, 255, .7);  opacity: 0;  border-radius: 100%;"
						+ "  transform: scale(1, 1) translate(-50%);  transform-origin: 50% 50%;")
				
				.on(CSSSelector.onPath(cRippleEffectColorBack, ":after"), "background: rgba(0, 0, 0, 0.1)")
				
				.on("@keyframes rippleanim", 
						"0% {transform: scale(0, 0); opacity: 1; }"
						+"50% {transform: scale(30, 25); opacity: 1;  } "
						+"100% {opacity: 0;transform: scale(80, 40);  }")
				
				.on(CSSSelector.onPath(cRippleEffect, cRippleEffectShow, ":after"),"animation: rippleanim "+ SPEED_RIPPLE_EFFECT +"ms ease-out;" )
				
				;
	}	
		
	@xCoreVersion("1")
	@xExport
	public interface JSRippleEffect extends JSClass
	{
		JSNodeElement ripple = JSClass.declareType();
		TActionEvent actionEvent = JSClass.declareType();
		JSActionManager actionManager = JSClass.declareTypeClass(JSActionManager.class);
		
		@xStatic(autoCall = true)
		default void init() {
			actionManager.onStart(var(fct(actionEvent, ()->{
				/****************************************************/
				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
					let(ripple, searchRipple(actionEvent.actionTarget()));
					_if(ripple.notEqualsJS(null)).then(() -> {
						__(TKQueue.startProcessQueued(
								NEXT_FRAME,
								fct(() -> ripple.classList().add(cRippleEffectShow)),
								SPEED_RIPPLE_EFFECT,
								fct(() -> ripple.classList().remove(cRippleEffectShow))));
					});
				});
			}),".bind(this)"));
		}
		
		@xStatic()
		default JSNodeElement searchRipple(JSNodeElement target) {
			// recherche le ripple btn
			let(ripple, target);

			_if("!", ripple.classList().contains(cRippleEffect))
					.then(() -> ripple.set(target.closest(cRippleEffect)));

			_if(ripple.equalsJS(null))
					.then(() -> ripple.set(target.querySelector(cRippleEffect)));

			return ripple;
		}
	}
	
	/* 
button {
  border: none;
  cursor: pointer;
  color: white;
  padding: 15px 40px;
  border-radius: 2px;
  font-size: 22px;
  box-shadow: 2px 2px 4px rgba(0, 0, 0, .4);
  background: #2196F3;
}


button{
  position: relative;
  overflow: hidden;
}

button:after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, .5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}

@keyframes ripple {
  0% {
    transform: scale(0, 0);
    opacity: 1;
  }
  20% {
    transform: scale(25, 25);
    opacity: 1;
  }
  100% {
    opacity: 0;
    transform: scale(40, 40);
  }
}

button:focus:not(:active)::after {
  animation: ripple 1s ease-out;
}

*/
	
	
	/*
	 *
(function (window, $) {
  
  $(function() {
    
    
    $('.ripple').on('click', function (event) {
      event.preventDefault();
      
      var $div = $('<div/>'),
          btnOffset = $(this).offset(),
      		xPos = event.pageX - btnOffset.left,
      		yPos = event.pageY - btnOffset.top;
      

      
      $div.addClass('ripple-effect');
      var $ripple = $(".ripple-effect");
      
      $ripple.css("height", $(this).height());
      $ripple.css("width", $(this).height());
      $div
        .css({
          top: yPos - ($ripple.height()/2),
          left: xPos - ($ripple.width()/2),
          background: $(this).data("ripple-color")
        }) 
        .appendTo($(this));

      window.setTimeout(function(){
        $div.remove();
      }, 2000);
    });
    
  });
  
})(window, jQuery);
 
 
 ----------------------------------------------------------------------
 .ripple{
  overflow:hidden;
}

.ripple-effect{
  position: absolute;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  background: white;

    
  animation: ripple-animation 2s;
}


@keyframes ripple-animation {
    from {
      transform: scale(1);
      opacity: 0.4;
    }
    to {
      transform: scale(100);
      opacity: 0;
    }
}
	 */
}
