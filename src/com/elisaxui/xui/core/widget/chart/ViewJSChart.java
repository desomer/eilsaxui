/**
 * 
 */
package com.elisaxui.xui.core.widget.chart;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

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
	public Element xContenu() {
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
          "animation : {easing : \"easeOutBounce\", animateScale : false }"+
//		//	"animationEasing : \"easeOutBounce\","+
//			"animateRotate : true,"+
//			"animateScale : true,"+
//		//	"onAnimationComplete : null"+
			"}";
	
	
	String doughnutData ="{"+
			"labels: ["+
			         " \"Red\","+
			         " \"Blue\","+
			         " \"Yellow\""+
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
	public Element xAddJS() {
		return xScriptJS(js()
				  .var("options", doughnutOptions)
				  .var("data", doughnutData)
				  .var("ctx", "document.getElementById('" + getPropertyXID(ID).getValue()+"').getContext('2d')")
//				  .consoleDebug("'ctx'", "ctx")
				  .var("mydoughnutChart", "new Chart(ctx, {type: 'doughnut', data: data, options : options })")	
				);
	}
	
}
