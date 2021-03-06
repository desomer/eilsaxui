/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.component.page.old.ConfigScene;
import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;

/**
 * @author gauth
 *
 */
@xResource(id = "shop")
@xComment("activite shop")
@xCoreVersion("0")
public class ScnShop extends XUIScene {

	
	@Override
	public JSContentInterface createScene()
	{
		return fragment()
		;
	}

	@Override
	public JSContentInterface loadPage()
	{
		return fragment()
			.__(tkActivity.createActivity(new JSONPageHome().getJSON()))
			.__(tkActivity.prepareActivity(new JSONPageDetail().getJSON()))


		;
	}
	
	
	ConfigScene conf = new ConfigScene();

	@Override
	public ConfigScene getConfigScene() {
		
	    conf.setBgColorMenu("linear-gradient(to right, rgba(1, 162, 239, 0.5) 0%, rgba(0, 208, 255, 0.68) 36%, rgb(94, 165, 253) 100%);");
		conf.setBgColorNavBar("linear-gradient(to right, rgb(94, 194, 253) 0%, rgb(45, 235, 255) 64%, rgb(68, 176, 239) 100%);");
		conf.setBgColorTheme("#44b0ef");
		conf.setIdIcon("memo");
		conf.setTitle("Memo");
		return conf;
	}
	
	
}
