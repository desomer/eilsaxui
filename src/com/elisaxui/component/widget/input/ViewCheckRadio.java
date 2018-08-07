/**
 * 
 */
package com.elisaxui.component.widget.input;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
public class ViewCheckRadio extends XHTMLPart implements ICSSBuilder {

	public static VProperty pLabel;
	public static VProperty pValue;
	
	CSSClass checkgroup;
	
	@xTarget(CONTENT.class)
	public XMLElement xDesign() {
		return xDiv(checkgroup,
				xInput(xAttr("type", xTxt("checkbox")),	pValue ),
				xLabel(pLabel)
				);
	}

	@xTarget(HEADER.class)
	@xResource(id="input.css")
	public XMLElement xStylePart() {

		return xElem(
				xStyle(sMedia("all"), () -> {
					sOn(sSel(checkgroup), () -> {
						css("margin-top: 15px;");
						sOn(sSel("label"), ()-> {
							css("font-size: 18px; padding:10px 10px 2px 5px;color: #5264AE");
						});
						sOn(sSel("input[type='checkbox']"), () -> {
							css("width:18px;appearance: none;-webkit-appearance: none; outline: none; position: relative;border: none;");
							css("cursor: pointer; vertical-align: -6px;outline-width: 0;border-radius: 50%;");
							sOn(sSel("&:focus,&:after"), () -> {
								css("content: ''");
							});
							sOn(sSel("&:after"), () -> {
								css("display: block;width: 14px;height: 14px;margin-top: -2px;margin-right: 5px");
								css("border: 2px solid #666;border-radius: 2px;background-color: #fff;z-index: 999;");   //transition: 240ms
							});
							
							sOn(sSel("&:checked"), () -> {
								sOn(sSel("&:after"), () -> {
									css("background-color:#009688;border-color:#009688");
								});	
								sOn(sSel("&:before"), () -> {
									css("content:'';position: absolute;top: 0;left: 6px;display: table;width: 4px;height: 10px;border: 2px solid #fff;");
									css("border-top-width: 0;border-left-width: 0;transform: rotate(45deg)");
								});	
							});							
						});
					});
				}));
				
	}
	
	/*
	input[type="checkbox"], input[type="radio"] {
		  width: $base-size;
		  appearance: none;
		  -webkit-appearance: none;
		  outline: none;
		  position: relative;
		  border: none;
		  cursor: pointer;
		  margin-left: -25px;
		  vertical-align: -4px;
		  outline-width: 0;
		  border-radius: 50%;
		  
		  &:focus{
		    animation: press .8s 1;  
		  }
		  
		  &:before, &:after {
		    content: "";
		  }
		  
		  &:after {
		    
		    display: block;
		    width: 14px;
		    height: 14px;
		    margin-top: -2px;
		    margin-right: 5px;
		    border: 2px solid #666;
		    transition: 240ms;
		  }
		  
		  &:disabled {
		    &:after {
		      border-color: #ddd;
		      background-color: #eee;
		      cursor: not-allowed;
		    }
		    
		    &:checked {
		      &:after {
		        background-color: #eee;
		        border-color: #ddd;
		      }
		      
		      &:before {
		        border-color: #999;
		      }
		    }
		  }
		}
		input[type="checkbox"] {
		  
		  &:after {
		    border-radius: 2px;
		    background-color: #fff;
		    z-index: 999;
		  }
		  
		  &:checked{
		    &:after {
		      background-color: $cMain-color;
		      border-color: $cMain-color;
		    }
		    &:before {
		      content: "";
		      position: absolute;
		      top: 0;
		      left: 6px;
		      display: table;
		      width: 4px;
		      height: 10px;
		      border: 2px solid #fff;
		      border-top-width: 0;
		      border-left-width: 0;
		      transform: rotate(45deg);
		    }
		  }
		}

		input[type="radio"] {

		   &:before{
		     position: absolute;
		     left: 0;
		     top: 0;
		     background-color: $cMain-color;
		     width: $base-size;
		     height: $base-size;

		     display: block;
		     margin-top: -2px;
		     border-radius: 50%;
		     transition: 240ms;
		     transform: scale(0);
		   }
		  
		   &:after {
		    border-radius: 50%;
		  }
		  
		  &:checked{
		    &:before {
		        transform: scale(.5);   
		    }
		    &:after {
		      border-color: $cMain-color;
		    }
		    
		    &:disabled{
		      &:before{
		        background-color: #ccc;
		      }
		    }
		  }
		}
	*/
}
