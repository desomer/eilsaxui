/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.xui.core.page.ConfigScene;
import com.elisaxui.xui.core.page.XUIScene;

/**
 * @author gauth
 *
 */
@xFile(id = "shop")
@xComment("activite shop")
public class ScnShop extends XUIScene {

	
	@Override
	public JSMethodInterface createScene()
	{
		return fragment()
		;
	}

	@Override
	public JSMethodInterface loadPage()
	{
		return fragment()
		.__(tkActivity.createActivity(new JSONPageMain().getJSON()))
		;
	}
	
	
	ConfigScene conf = new ConfigScene();

	@Override
	public ConfigScene getConfigScene() {
		
	    conf.setBgColorMenu("background: linear-gradient(to right, rgba(1, 162, 239, 0.5) 0%, rgba(0, 208, 255, 0.68) 36%, rgb(94, 165, 253) 100%);");
		conf.setBgColorNavBar("background: linear-gradient(to right, rgb(94, 194, 253) 0%, rgb(45, 235, 255) 64%, rgb(68, 176, 239) 100%);");
		conf.setBgColorTheme("#44b0ef");
		return conf;
	}
	
	
}
