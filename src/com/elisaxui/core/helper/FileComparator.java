/**
 * 
 */
package com.elisaxui.core.helper;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.app.ConfigFormat;

import difflib.DiffRow;
import difflib.DiffRowGenerator;

/**
 * @author gauth
 *
 */
public class FileComparator {
	public StringBuilder listLineDiff = new StringBuilder(10*1024);
	
	
	public void initLineDiff(String htmlInCache, String htmlInCacheOriginal, boolean modeXml) {
		
		listLineDiff.setLength(0);
		Formatter formatter = new Formatter(listLineDiff, Locale.FRENCH);
		
		String sep = modeXml?"":"//";
		
		if (ConfigFormat.getData().isFileChanged())
		{
			listLineDiff.append("\n");
			for (File file : XUIFactory.changeMgr.listFileChanged) {
				listLineDiff.append(sep+"\t"+file);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(file.lastModified());
				formatter.format("\t [%1$tT %1$tD]%n", cal);
			} 
		}
		
		formatter.close();
		
		if (ConfigFormat.getData().isPatchChanges())
		{
			List<String> original = Arrays.asList(htmlInCache.split("\n"));
			List<String> revised = Arrays.asList(htmlInCacheOriginal.split("\n"));
			
			DiffRowGenerator generator = new DiffRowGenerator.Builder().InlineNewTag("span").InlineNewCssClass("cnew").InlineOldTag("span").InlineOldCssClass("cold").ignoreBlankLines(true).showInlineDiffs(true).ignoreWhiteSpaces(true).columnWidth(100).build();
			List<DiffRow> text = generator.generateDiffRows(original, revised);
			int numLine = 1;
			for (DiffRow diffRow : text) {		 
				 if (!diffRow.getTag().equals(DiffRow.Tag.EQUAL))
				 {
					
					 if (diffRow.getTag().equals(DiffRow.Tag.DELETE))
					 {
						 numLine--; 
						 listLineDiff.append("\n"+sep+"\t"+String.format ("%05d", numLine) + "-" +String.format ("%05d", numLine+1) + ":"+ diffRow.getTag()  + ": " + diffRow.getOldLine()+"  =>  " + diffRow.getNewLine());
					 }
					 else
						 listLineDiff.append("\n"+sep+"\t"+String.format ("%05d", numLine) + ":"+ diffRow.getTag()  + ": " + diffRow.getOldLine()+"  =>  " + diffRow.getNewLine());
				 } 
				 numLine++;
			}
		}
	}
	
}
