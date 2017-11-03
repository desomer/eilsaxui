/**
 * 
 */
package com.elisaxui.xui.core.page;

/**
 * @author gauth
 *
 */
public class ConfigScene {


	private String bgColorScene = "#333333"; // "#333333"
	private String bgColorTheme = "#ff359d";  // rose
	private String bgColorThemeOpacity = "rgba(255,0,136,1)";
	private String bgColorNavBar = "background: linear-gradient(to right, rgba(253,94,176,1) 0%, rgba(255,0,136,1) 64%, rgba(239,1,124,1) 100%);";
	private String bgColorMenu = "background: linear-gradient(to right, rgba(239,1,124,0.5) 0%, rgba(255,0,136,0.68) 36%, rgba(253,94,176,1) 100%);";
	private String bgColorContent = "rgb(245, 243, 237)";   // gris
	private String app_manifest = "/rest/json/manifest.json";
	private String idicon = "elisys";
	private String title = "Elisys";
	
	

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the id_icon
	 */
	public String getIdIcon() {
		return idicon;
	}
	/**
	 * @param id_icon the id_icon to set
	 */
	public void setIdIcon(String id_icon) {
		this.idicon = id_icon;
	}
	/**
	 * @return the bgColorScene
	 */
	public String getBgColorScene() {
		return bgColorScene;
	}
	/**
	 * @param bgColorScene the bgColorScene to set
	 */
	public void setBgColorScene(String bgColorScene) {
		this.bgColorScene = bgColorScene;
	}
	/**
	 * @return the bgColorTheme
	 */
	public String getBgColorTheme() {
		return bgColorTheme;
	}
	/**
	 * @param bgColorTheme the bgColorTheme to set
	 */
	public void setBgColorTheme(String bgColorTheme) {
		this.bgColorTheme = bgColorTheme;
	}
	/**
	 * @return the bgColorThemeOpacity
	 */
	public String getBgColorThemeOpacity() {
		return bgColorThemeOpacity;
	}
	/**
	 * @param bgColorThemeOpacity the bgColorThemeOpacity to set
	 */
	public void setBgColorThemeOpacity(String bgColorThemeOpacity) {
		this.bgColorThemeOpacity = bgColorThemeOpacity;
	}
	/**
	 * @return the bgColor
	 */
	public String getBgColorNavBar() {
		return bgColorNavBar;
	}
	/**
	 * @param bgColor the bgColor to set
	 */
	public void setBgColorNavBar(String bgColor) {
		this.bgColorNavBar = bgColor;
	}
	/**
	 * @return the bgColorMenu
	 */
	public String getBgColorMenu() {
		return bgColorMenu;
	}
	/**
	 * @param bgColorMenu the bgColorMenu to set
	 */
	public void setBgColorMenu(String bgColorMenu) {
		this.bgColorMenu = bgColorMenu;
	}
	/**
	 * @return the bgColorContent
	 */
	public String getBgColorContent() {
		return bgColorContent;
	}
	/**
	 * @param bgColorContent the bgColorContent to set
	 */
	public void setBgColorContent(String bgColorContent) {
		this.bgColorContent = bgColorContent;
	}
	
	
	/**
	 * @return the app_manifest
	 */
	public String getAppManifest() {
		return app_manifest;
	}
	/**
	 * @param app_manifest the app_manifest to set
	 */
	public void setAppManifest(String app_manifest) {
		this.app_manifest = app_manifest;
	}
}
