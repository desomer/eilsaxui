package com.elisaxui.core.xui.view;

import com.elisaxui.core.xui.XUIFactoryScene;
import com.elisaxui.core.xui.XUIPageBuilder;

public abstract class XUIView {

	public abstract void doView();
	
	
	public XUIView vBody(Object body)
	{
		XUIFactoryScene.getXUIPageBuilder().addPart(XUIPageBuilder.HtmlPart.BODY, body);
		return this;
	}
}
