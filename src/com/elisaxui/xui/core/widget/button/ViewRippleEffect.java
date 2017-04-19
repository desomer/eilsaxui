/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

/**
 * @author Bureau
 *
 */
public class ViewRippleEffect extends XHTMLPart {

    static ViewRippleEffect style = new ViewRippleEffect();
	static {
		xPart(style);
	}
	public static CSSClass cRippleEffect()
	{
		return style.cRippleEffect;
	}
	
	CSSClass cRippleEffect;
	
	

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on(cRippleEffect, "overflow: hidden; ")  /*position: relative;*/
				.on(CSSSelector.onPath(cRippleEffect, ":after"), " content: ''; "
						+ "position: absolute; top: 50%;  left: 50%;  width: 5px;  height: 5px;"
						+ "  background: rgba(255, 255, 255, .7);  opacity: 0;  border-radius: 100%;"
						+ "  transform: scale(1, 1) translate(-50%);  transform-origin: 50% 50%;")
				.on("@keyframes rippleanim", 
						"0% {transform: scale(0, 0); opacity: 1; }"
						+ "20% {transform: scale(25, 25); opacity: 1;  } "
						+ "100% {opacity: 0;transform: scale(40, 40);  }")
				.on(CSSSelector.onPath(cRippleEffect,":focus:not(:active)::after"),"animation: rippleanim 500ms ease-out;" )
				
				;
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
