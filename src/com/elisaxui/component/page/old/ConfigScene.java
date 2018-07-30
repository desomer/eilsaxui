/**
 * 
 */
package com.elisaxui.component.page.old;

/**
 * @author gauth
 *
 */
@Deprecated
public class ConfigScene {


	private String bgColorScene = "#333333"; // gris noir
	private String bgColorTheme = "#ff359d";  // rose
	private String bgColorNavBar = "linear-gradient(to bottom, rgba(253, 94, 176, 0.45) 0%, rgba(255,0,136,1) 64%, rgba(239,1,124,1) 100%);";
	private String bgColorMenu = "linear-gradient(to top, rgb(255, 54, 158) 0%, rgba(255,0,136,0.68) 36%, rgb(216, 98, 159) 100%);";
	private String bgColorContent = "rgb(245, 243, 237)";   // gris clair
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
