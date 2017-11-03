/**
 * 
 */
package com.elisaxui.app.elisys.xui.asset;

import java.util.HashMap;
import java.util.Map;

import com.elisaxui.ResourceLoader;

/**
 * @author gauth
 *
 */
public class AssetHandler {

		public static final Map<String, ResourceLoader> dicoAsset = new HashMap<>();
		
		static {
			dicoAsset.put("elisys", new ResourceLoader(AssetHandler.class, "elisys.png"));
			dicoAsset.put("memo", new ResourceLoader(AssetHandler.class, "memo.png"));
		}
		
		public static final String getIconUri(String id, int h, int w)
		{
			return "/rest/json/icon-"+id+"-"+h+"x"+w+".png";
		}

}
