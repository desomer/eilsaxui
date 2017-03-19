package com.elisaxui.core.xui.view;

import com.elisaxui.core.xui.XUIFactoryScene;
import com.elisaxui.core.xui.XUIHtmlBuilder;
import com.elisaxui.core.xui.XUIHtmlBuilder.Tag;
import com.elisaxui.core.xui.XUIPageBuilder;

public abstract class XUIView {

	public abstract void doView();
	
	XUIHtmlBuilder htmlBuilder = new XUIHtmlBuilder(null);
	
	
	public Tag xTag(String tag, Object...objects )
	{
		Tag t = htmlBuilder.getTag();
		t.name = tag;
		return t;
	}
	
	public XUIView vBody(Object body)
	{
		XUIFactoryScene.getXUIPageBuilder().addPart(XUIPageBuilder.HtmlPart.BODY, body);
		return this;
	}
}
