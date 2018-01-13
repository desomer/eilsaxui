/**
 * 
 */
package com.elisaxui.component.widget.chart;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewJSChart extends XHTMLPart {

	public static final String ID = "ID";
	

	public ViewJSChart(Object id) {
		super();
		this.addProperty(ID, id);
	}



	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xCanvas(this.getProperty(ID), xAttr("width", "100%")/* , xAttr("height", "300px")*/ ) ;
		
	}
	
	String doughnutOptions="{"+
//			"segmentShowStroke : true,"+
//			"segmentStrokeColor : \"#fff\","+
//			"segmentStrokeWidth : 2,"+
//			"percentageInnerCutout : 50,"+
//			"animation : true,"+
//			"animationSteps : 100,"+
          "responsive: true, "+
          "animation : {easing : \"easeOutBounce\", animateScale : true }"+
//		//	"animationEasing : \"easeOutBounce\","+
//			"animateRotate : true,"+
//			"animateScale : true,"+
//		//	"onAnimationComplete : null"+
			"}";
	
	
	String doughnutData ="{"+
			"labels: ["+
			         " \"Bonne\","+
			         " \"Mauvaise\","+
			         " \"A Faire\""+
			         " ],"+
			         " datasets: ["+
			         " {"+
			         " data: [300, 50, 100],"+
			         " backgroundColor: ["+
			         " \"#FF6384\","+
			         " \"#36A2EB\","+
			         " \"#FFCE56\""+
			         " ],"+
			         " hoverBackgroundColor: ["+
			         " \"#FF6384\","+
			         " \"#36A2EB\","+
			         " \"#FFCE56\""+
			         " ]"+
			         " }]"+
	"}";

	
	
	@xTarget(AFTER_CONTENT.class)
	public XMLElement xAddJS() {
		return xScriptJS(js()
				//  .__("Chart.defaults.global.maintainAspectRatio = false") 
				  ._var("options", doughnutOptions)
				  ._var("data", doughnutData)
				  ._var("ctx", "document.getElementById('" + getPropertyXID(ID).getValue()+"').getContext('2d')")
				  ._var("mydoughnutChart", "new Chart(ctx, {type: 'doughnut', data: data, options : options })")	
				);
	}
	
}
