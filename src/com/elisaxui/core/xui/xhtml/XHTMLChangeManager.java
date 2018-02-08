/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

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

import difflib.DiffRow;
import difflib.DiffRowGenerator;

/**
 * @author gauth
 *
 */
public class XHTMLChangeManager {

	public Map<String, Class<? extends XHTMLPart>> mapClass = new HashMap<String, Class<? extends XHTMLPart>>(100);
	public List<File> listFileChanged = new ArrayList<>();
	public StringBuilder listLineDiff = new StringBuilder();
	
	public int nbChangement = 0;
	public long lastOlderFile = 0;
	public LocalDateTime dateBuild = null;
	
	/**
	 * @param listLineDiff
	 * @param htmlInCache
	 * @param htmlInCacheOriginal
	 */
	public void initLineDiff(String htmlInCache, String htmlInCacheOriginal) {
		
		listLineDiff.setLength(0);
		Formatter formatter = new Formatter(listLineDiff, Locale.FRENCH);
		
		listLineDiff.append("\n");
		for (File file : listFileChanged) {
			listLineDiff.append("\t"+file);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(file.lastModified());
			formatter.format("\t [%1$tT %1$tD]%n", cal);
		} 
		
		
		List<String> original = Arrays.asList(htmlInCache.split("\n"));
		List<String> revised = Arrays.asList(htmlInCacheOriginal.split("\n"));

//				Patch patch = DiffUtils.diff(original, revised);
//				for (Delta delta: patch.getDeltas()) {
//		                System.out.println(delta);
//		        }
		
		DiffRowGenerator generator = new DiffRowGenerator.Builder().InlineNewTag("span").InlineNewCssClass("cnew").InlineOldTag("span").InlineOldCssClass("cold").ignoreBlankLines(true).showInlineDiffs(true).ignoreWhiteSpaces(true).columnWidth(100).build();
		List<DiffRow> text = generator.generateDiffRows(original, revised);
		int numLine = 1;
		for (DiffRow diffRow : text) {		 
			 if (!diffRow.getTag().equals(DiffRow.Tag.EQUAL))
			 {
				
				 if (diffRow.getTag().equals(DiffRow.Tag.DELETE))
				 {
					 numLine--; 
					 listLineDiff.append("\n\t"+String.format ("%05d", numLine) + "-" +String.format ("%05d", numLine+1) + ":"+ diffRow.getTag()  + ": " + diffRow.getOldLine()+"  =>  " + diffRow.getNewLine());
				 }
				 else
					 listLineDiff.append("\n\t"+String.format ("%05d", numLine) + ":"+ diffRow.getTag()  + ": " + diffRow.getOldLine()+"  =>  " + diffRow.getNewLine());
			 } 
			 numLine++;
		}
		
	}
	
	
}
