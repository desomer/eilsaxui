/**
 * 
 */
package com.elisaxui.core.helper.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author gauth
 *
 */
public class CoreLogger {

	private CoreLogger() {
		super();
	}
	
	static ConcurrentHashMap<String, Logger> mapLogger = new ConcurrentHashMap<>();

	public static Logger getLogger(int nb) {
		
		String name = Thread.currentThread().getStackTrace()[nb].getClassName();
		
		Logger logger = mapLogger.computeIfAbsent(name, key-> {
			Handler handler = new ConsoleHandler();
			handler.setLevel(Level.ALL);
			
			handler.setFormatter(new SingleLineFormatter());
	
			Logger l = Logger.getLogger(name);
			l.addHandler(handler);
			l.setUseParentHandlers(false);
			l.setLevel(Level.FINE);
			return l;
		});


		return logger;
	}
	
	public static class SingleLineFormatter extends Formatter {

		  private static final String FORMAT = "{0,date,short} {0,time}";
		
		  Date dat = new Date();
		  private MessageFormat formatter;
		  private Object[] args = new Object[1];

		  private String lineSeparator = "\n";

		  /**
		   * Format the given LogRecord.
		   * @param record the log record to be formatted.
		   * @return a formatted log record
		   */
		  public synchronized String format(LogRecord record) {

		    StringBuilder sb = new StringBuilder();

		    // Minimize memory allocations here.
		    dat.setTime(record.getMillis());    
		    args[0] = dat;


		    // Date and time 
		    StringBuffer text = new StringBuffer();
		    if (formatter == null) {
		      formatter = new MessageFormat(FORMAT, Locale.FRENCH);
		    }
		    formatter.format(args, text, null);
		    sb.append(text);
		    sb.append(" <");


		    // Class name 
		    if (record.getSourceClassName() != null) {
		      sb.append(record.getSourceClassName());
		    } else {
		      sb.append(record.getLoggerName());
		    }

		    // Method name 
		    if (record.getSourceMethodName() != null) {
		      sb.append(":");
		      sb.append(record.getSourceMethodName());
		      sb.append(">");
		    }

		    // Level
		    sb.append("[");
		    sb.append(record.getLevel().getLocalizedName());
		    sb.append("] ");

		    // Indent - the more serious, the more indented.
		    int iOffset = (1000 - record.getLevel().intValue()) / 100;
		    for( int i = 0; i < iOffset;  i++ ){
		      sb.append(" ");
		    }

		    String message = formatMessage(record);
		    sb.append(message);
		    
		    sb.append(lineSeparator);
		    if (record.getThrown() != null) {
		      try {
		        StringWriter sw = new StringWriter();
		        PrintWriter pw = new PrintWriter(sw);
		        record.getThrown().printStackTrace(pw);
		        pw.close();
		        sb.append(sw.toString());
		      } catch (Exception ex) {
		    	 // ne fait rien
		      }
		    }
		    return sb.toString();
		  }
		}
	
}
