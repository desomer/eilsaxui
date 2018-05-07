/**
 * 
 */
package com.elisaxui.core.xui.config;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;

import difflib.DiffRow;
import difflib.DiffRowGenerator;

/**
 * @author gauth
 *
 */
public class XHTMLChangeManager {

	public Map<String, Class<? extends XHTMLPart>> mapClass = new HashMap<String, Class<? extends XHTMLPart>>(100);
	public List<File> listFileChanged = new ArrayList<>();
	
	public int nbChangement = 0;
	public long lastOlderFile = 0;
	public LocalDateTime dateBuild = null;
		
	
}
