package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;

public interface JSTestDataDriven extends JSClass {

	JSDataDriven aDataDriven = null; 
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	
//	default Object constructor()
//	{
//		return set(aDataDriven, null)
//			.set(aDataSet, null)	;
//	}
	
	default Object createRow()
	{
		return __("return ",
				fct("value")
	            .set(template, ScnAdminMain.xTemplateDataDriven("value.a", "value.b"))
	            .__(template.append("$('body')"))  		
			);
	}
	
	default Object startTest()
	{
		return var("v", " []")    // {a:15, b:'12'},{a:21, b:'22'} 
				
				.set(aDataSet, _new())
				.__(aDataSet.setData("v"))
				.set(aDataDriven, _new(aDataSet))
				.__(aDataDriven.onEnter(fct("value")
						._if("value.row['_dom_']==null")
				            .set(template, ScnAdminMain.xTemplateDataDriven("value.row.a", "value.row.b"))
				            //.var("jqdom", template.insertAt("$('#content')", "value.idx"))
				            .var("jqdom", template.append("$('#content')"))
				            .__("value.row['_dom_']=jqdom[0]")
		            		.__("jqdom.css('position','absolute')"
		            		+" .css('transition','transform 500ms ease-out')"
		            		+" .css('transform','translate3d(0px,' + value.idx*17 + 'px,0px)')"
		            		)
			            .endif()
						//.__("$(value.row['_dom_']).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',  function() {this.removeClass('bounceInLeft');}.bind($(value.row['_dom_'])) )")
//			            .__("$(value.row['_dom_'])"
//			            		//+ ".css('transform','scale3d(1,0,1)')"
//			            		//+ ".css('transform-origin','0px 50%')"
//			            		//+ ".css(\"margin-left\", \"150px\").animate({\"margin-left\" : \"0px\"}, 500)"
//			            		//+".css('animation-duration','1s')"
//			            		//+".css('transform-origin','30px 50%')"
//			            		//+ ".addClass('animated bounceInLeft')"
//			            		+" .css('position','absolute')"
//			            		+" .css('transition','transform 100ms ease-out')"
//			            		+" .css('transform','translate3d(0px,' + value.idx*17 + 'px,0px)')"
//			            		)

			            
	            ))
				.__(aDataDriven.onExit(fct("value")
						._if("value!=null && value.row['_dom_']!=null")
						//	.__("$(value.row['_dom_']).addClass('zoomOut')")
						//	.__("$(value.row['_dom_']).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',  function() {this.remove();} )")
						//	.__("value.row['_dom_']=null")
							//.__("$(value.row['_dom_']).animate({\"margin-left\" : \"150px\"}, 500)")  
						.endif()
					))
				
				.__(aDataDriven.onChange(fct("value")
						._if("value.row['_dom_']!=null")
						    //.consoleDebug("value")
							.__("$(value.row['_dom_']).css('transform','translate3d(0px,' + value.value*17 + 'px,0px)')")
						.endif() 
						))
				
				.__(aDataDriven.start())
				
				.set("v", aDataSet.getData())
				//.__("v.push( {a:45, b:'test'} )")
				
//				.__("setTimeout(", fct()
//						._for("var i=0; i<100; i++")
//							.__("v.push( {a:i, b:'test5'} )") 
//						.endfor()	
//							, ", 1000)")
				
				.__("setTimeout(", fct()
						._for("var i=0; i<20; i++")
							//.__("setTimeout(function(idx) {v.splice(0,0, {a:idx, b:'test5'} )}, i*10, i)") 
							.__("setTimeout(function(idx) {v.push({a:idx, b:'test5'})} , i*10, i)") 
						.endfor()	
							, ", 1000)")
				
//				.__("setTimeout(", fct()
//						._for("var i=0; i<100; i++")
//							.__("setTimeout(function() {v.splice(99-i,1)}, i*10)") 
//						.endfor()	
//							, ", 3000)")
				
				.__("setTimeout(", fct()
						._for("var i=0; i<10000; i++")
							.__("setTimeout(function() {var idx = Math.floor(Math.random()*(v.length));\n"
						        +"var idx2 = Math.floor(Math.random()*(v.length));\n"
							    + "   var s = v.splice(idx, 1)\n; "  // retire
							    + "v.splice(idx2,0,s[0]); "    // ajoute 
							    +"for(var i=0; i<v.length; i++) {"  // recalcule
							    + " v[i].idx=i"
							    + "}"
							    +"}, i*100)") 
						.endfor()	
							, ", 3000)")
				;
		
		/*
		 * for (var i = 0; i < array.length ; i++) {
     temp.push(array.splice(Math.floor(Math.random()*array.length),1));
   }
		 */
	}
}
