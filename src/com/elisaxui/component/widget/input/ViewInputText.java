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
public class ViewInputText extends XHTMLPart implements ICSSBuilder {

	public static VProperty pLabel;
	public static VProperty pValue;
	
	CSSClass group;
	CSSClass highlight;
	CSSClass bar;

	@xTarget(CONTENT.class)
	public XMLElement xDesign() {
		return xDiv(group,
				xInput(xAttr("type", xTxt("text")), xAttr("required"), 
						vIfExist(pValue, xAttr("value", pValue))),
				xSpan(highlight),
				xSpan(bar),
				xLabel(pLabel));
	}

	@xTarget(HEADER.class)
	@xResource(id="input.css")
	public XMLElement xStylePart() {

		return xElem(
				xStyle(sMedia("all"), () -> {
				//	sOn(sSel("*"), () -> css("box-sizing:border-box;"));
					sOn(sSel(group), () -> {
						css("position:relative;  margin-top:15px");
						// input
						sOn(sSel("input"), () -> {
							css("font-size:18px; padding:10px 10px 2px 5px; display:block;");
							css("width:300px;border:none;border-bottom:1px solid #757575;");
							css("background-color:inherit");
							sOn(sSel("&:focus"), () -> {
								css("outline:none;");
								// label
								sOn(sSel("~ label"), () -> css("top:-10px;font-size:14px;color:#5264AE;"));
								// bar
								sOn(sSel("~ ", bar, ":before,~ ", bar, ":after"), () -> css("width:50%;"));
								// highlight
								sOn(sSel("~", highlight), () -> css("animation:inputHighlighter 0.3s ease"));
							});

							sOn(sSel("&:valid ~ label"), () -> css("top:-10px;font-size:14px;color:#5264AE;"));

						});
						// label
						sOn(sSel("label"), () -> {
							css("color:#999; font-size:18px; font-weight:normal; position:absolute; pointer-events:none;");
							css("left:5px; top:10px; transition:0.2s ease all;");
						});
						// bar
						sOn(sSel(bar), () -> {
							css("position:relative; display:block; width:300px;");
							sOn(sSel("&:before,&:after"), () -> {
								css("content:''; height:2px; width:0px; bottom:1px; position:absolute;");
								css("background:#5264AE; transition:0.2s ease all;");
							});
							sOn(sSel("&:before"), () -> css("left:50%;"));
							sOn(sSel("&:after"), () -> css("right:50%;"));
						});
						// highlight
						sOn(sSel(highlight), () -> {
							css("position:absolute;height:60%;width:300px;top:25%;left:0;pointer-events:none;opacity:0.5;");
						});
					});

				}),

				// animation
				xStyle(sKeyFrame("inputHighlighter"), () -> {
					sOn(sSel("from"), () -> css("background:#5264AE;"));
					sOn(sSel("to"), () -> css("width:0; background:transparent;"));
				}));
	}

}

// https://codepen.io/sammurphey/pen/BQvZbq
// ex : combobox
// background: url(data:image/svg+xml;utf8,<svg fill='#007BED' height='24'
// viewBox='0 0 24 24'…9-4.58L18 9.25l-6 6-6-6z'/> <path d='M0-.75h24v24H0z'
// fill='none'/> </svg>), no-repeat;
