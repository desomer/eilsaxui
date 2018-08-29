/**
 * 
 */
package com.elisaxui.core.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author gauth
 *
 */
public class ChromeManager {

	public static int openChrome(String url) {
		
		ProcessBuilder builder = new ProcessBuilder();
			
		int exitCode = 0;
		builder.command("cmd.exe", "/c", "start", "chrome.exe", "\""+url+"\"");
		try {
			builder.directory(new File(System.getProperty("user.home")));
			Process process = builder.start();
			StreamGobbler streamGobbler = 
			  new StreamGobbler(process.getInputStream(), System.out::println);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			exitCode= process.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exitCode;
	}

	
	private static class StreamGobbler implements Runnable {
	    private InputStream inputStream;
	    private Consumer<String> consumer;
	 
	    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
	        this.inputStream = inputStream;
	        this.consumer = consumer;
	    }
	 
	    @Override
	    public void run() {
	        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
	    }
	}
}
