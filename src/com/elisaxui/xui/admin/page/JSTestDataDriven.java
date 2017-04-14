package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface;
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
	
//	default JSInterface createRow()
//	{
//		return __("return ",
//				fct("value")
//	            .set(template, ScnAdminMain.xTemplateDataDriven("value.a", "value.b"))
//	            .__(template.append("$('body')"))  		
//			);
//	}
	
	default Object startTest()
	{
		 var("v", " []")    // {a:15, b:'12'},{a:21, b:'22'} 
				
				.set(aDataSet, _new())
				.__(aDataSet.setData("v"))
				
				.set(aDataDriven, _new(aDataSet))
				.__(aDataDriven.onEnter(fct("value")
						._if("value.row['_dom_']==null")
				            .set(template, ScnAdminMain.xTemplateDataDriven("value.row.a", "value.row.b"))
				            .var("jqdom", template.insertAt("$('#content')", "value.idx"))
				            .__("value.row['_dom_']=jqdom[0]")
		            		.__("jqdom.css('position','absolute')")
		            		.__("jqdom.css('right','0px')")
		            		.__("jqdom.css('left','0px')")
		            		.__("jqdom.css('transition','transform 500ms ease-out')"   // last idx pour vitesse de deplacement en fct de la distance
		            		+" .css('transform','translate3d(0px, ' + value.idx*35 + 'px,0px)')"
		            		)
			            .endif()
			            
			            //.var("jqdom", template.append("$('#content')"))
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
						//	.__("$(value.row['_dom_']).addClass('animated rubberBand infinite')")
							// .__("$(value.row['_dom_']).one('animationend',  function() {this.removeClass('animated rubberBand');}.bind($(value.row['_dom_'])) )")
							
						//	.__("$(value.row['_dom_']).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',  function() {this.remove();} )")
						//	.__("value.row['_dom_']=null")
							//.__("$(value.row['_dom_']).animate({\"margin-left\" : \"150px\"}, 500)")  
						.endif()
					))
				
				.__(aDataDriven.onChange(fct("value")
//					    .consoleDebug("value")
						._if("value.row['_dom_']!=null && value.property=='idx'")
							.__("$(value.row['_dom_']).css('transform','translate3d(0px,' + value.value*35 + 'px,0px)')")
						.endif() 
						._if("value.row['_dom_']!=null && value.property=='b'")
							.var("span","$(value.row['_dom_']).find('span:nth-child(2)')")
							.__("span.text(value.value)")
						.endif() 
						))
				
//				.__(aDataDriven.start())
				
				.set("v", aDataSet.getData())
				
				.__("setTimeout(", fct()
						._for("var i=0; i<20; i++")
							//.__("setTimeout(function(idx) {v.splice(0,0, {a:idx, b:'test5'} )}, i*10, i)") 
							.__("setTimeout(function(idx) {v.push({a:idx, b:'test5'})} , i*10, i)") 
						.endfor()	
							, ", 1)")
				
				.__("setTimeout(", fct()
						._for("var i=0; i<1000; i++")
							.__("setTimeout(function() {var idx = Math.floor(Math.random()*(v.length));\n"
						        +"var idx2 = Math.floor(Math.random()*(v.length));\n"
							    + "   var s = v.splice(idx, 1)\n; "  // retire
							    + "v.splice(idx2,0,s[0]); "    // ajoute 
							    
							    +"for(var i=0; i<v.length; i++) {"  // recalcule position
							    + " v[i].idx=i"
							    + "}"      // fin
							    
							    +"}, i*100)") 
						.endfor()	
							, ", 3000)")
				
				.__("setTimeout(", fct()
						._for("var i=0; i<1000; i++")
							.__("setTimeout(function() { var idx = Math.floor(Math.random()*(v.length));\nvar val = Math.floor(Math.random()*(100)); v[idx].b='test'+val; }, i*100)") 
						.endfor()	
				, ", 5000)")
				
				;
		
		 return null;
	}
}
