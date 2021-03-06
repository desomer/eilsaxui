package com.elisaxui.test;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

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
				
		 		JSArray v = let("v",new JSArray<>().asLitteral());
				_set(aDataSet, _new());
				aDataSet.setData(v);
				
				_set(aDataDriven, _new(aDataSet));
				
				aDataDriven.onEnter(funct("value")
						._if("value.row['_dom_']==null")
				            ._set(template, ScnAdminMain.xTemplateDataDriven("value.row.a", "value.row.b"))
				            ._var("jqdom", template.insertAt("$('#content')", "value.idx"))
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

			            
	            );
				aDataDriven.onExit(funct("value")
						._if("value!=null && value.row['_dom_']!=null")
						//	.__("$(value.row['_dom_']).addClass('animated rubberBand infinite')")
							// .__("$(value.row['_dom_']).one('animationend',  function() {this.removeClass('animated rubberBand');}.bind($(value.row['_dom_'])) )")
							
						//	.__("$(value.row['_dom_']).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',  function() {this.remove();} )")
						//	.__("value.row['_dom_']=null")
							//.__("$(value.row['_dom_']).animate({\"margin-left\" : \"150px\"}, 500)")  
						.endif()
					);
				
				aDataDriven.onChange(funct("value")
//					    .consoleDebug("value")
						._if("value.row['_dom_']!=null && value.property=='idx'")
							.__("$(value.row['_dom_']).css('transform','translate3d(0px,' + value.value*35 + 'px,0px)')")
						.endif() 
						._if("value.row['_dom_']!=null && value.property=='b'")
							._var("span","$(value.row['_dom_']).find('span:nth-child(2)')")
							.__("span.text(value.value)")
						.endif() 
						);
				
//				.__(aDataDriven.start())
				
				_set(v, aDataSet.getData())
				
				.__("setTimeout(", funct()
						._for("var i=0; i<20; i++")
							//.__("setTimeout(function(idx) {v.splice(0,0, {a:idx, b:'test5'} )}, i*10, i)") 
							.__("setTimeout(function(idx) {v.push({a:idx, b:'test5'})} , i*10, i)") 
						.endfor()	
							, ", 1)")
				
				.__("setTimeout(", funct()
						._for("var i=0; i<0; i++")
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
				
				.__("setTimeout(", funct()
						._for("var i=0; i<0; i++")
							.__("setTimeout(function() { var idx = Math.floor(Math.random()*(v.length));\nvar val = Math.floor(Math.random()*(100)); v[idx].b='test'+val; }, i*100)") 
						.endfor()	
				, ", 5000)")
				
				;
		
		 return null;
	}
}
