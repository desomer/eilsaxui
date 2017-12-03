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
			dicoAsset.put("babel", new ResourceLoader(AssetHandler.class, "babel.min.js"));
			dicoAsset.put("navgo", new ResourceLoader(AssetHandler.class, "navigo.js"));
		}
		
		public static final String getIconUri(String id, int h, int w)
		{
			return "/rest/json/icon-"+id+"-"+h+"x"+w+".png";
		}

}
