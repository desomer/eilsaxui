/**
 * 
 */
package com.elisaxui.core.cordova;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import javax.swing.filechooser.FileSystemView;
import javax.ws.rs.core.Response;

import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.CacheManager;
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.config.ConfigFormat;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;

/**
 * @author gauth
 *
 */
public class XHIFactoryCordova extends XUIFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		XHIFactoryCordova builder = new XHIFactoryCordova();

		builder.createHtml("ScnPage");

	}

	public void createHtml(String id) {
				
		doRep();
		
		RequestConfig requestConfig = new RequestConfig(null, null);

		requestConfig.setEs5(false);

		JSExecutorHelper.setThreadPreprocessor(requestConfig.isEs5());

		// cherche les changements
		initChangeManager();

		CacheManager cache = new CacheManager(id, "" + requestConfig.isEs5(), "html", false);

		ConfigFormat.getData().setSinglefile(true);

		Class<? extends XHTMLPart> xHTMLPartClass = changeMgr.mapClass.get(id);
		Response err = createVersion(requestConfig, cache, xHTMLPartClass);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    File apkFile = new File("C:\\Users\\gauth\\cordova\\test\\test\\platforms\\android\\app\\build\\outputs\\apk\\debug\\app-debug.apk");

	    long dateApk = apkFile.lastModified();
	    
		try {
			Path path = Paths.get("C:\\Users\\gauth\\cordova\\test\\test\\www\\index.html");
			byte[] strToBytes = cache.getResult().getBytes();
			
			Files.write(path, strToBytes);

			// String read = Files.readAllLines(path).get(0);

			CoreLogger.getLogger(1).info(() -> "OK HTML " + path);
			
			createApk();
			
			Path pathHuawei = Paths.get("Z:\\elysys");
			
			if (dateApk != apkFile.lastModified())
			{
				CoreLogger.getLogger(1).info(() -> "OK APK " + apkFile + " " +sdf.format(apkFile.lastModified()));
				Path pathApk = Paths.get("Z:\\elysys\\elisys.apk");
				Files.copy(apkFile.toPath(), pathApk, StandardCopyOption.REPLACE_EXISTING);
				CoreLogger.getLogger(1).info(() -> "OK COPY " + pathApk);
			}
						
			Stream<Path> listPath = Files.walk(pathHuawei);
			
		
			listPath.filter(Files::isRegularFile)
		        .forEach((p)->{
		        	CoreLogger.getLogger(1).info(() -> "->"+p.toFile()+ " " +sdf.format(p.toFile().lastModified()));
		        });
						
			listPath.close();
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void createApk() throws IOException, InterruptedException {
		String[] command = { "cmd.exe", "/C", "Start", "/D", "C:\\Users\\gauth\\Desktop\\cordova", "C:\\Users\\gauth\\Desktop\\cordova\\cordova.bat" };

		String line;
		Process process = Runtime.getRuntime().exec(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = input.readLine()) != null) {
			System.out.println(line);
		}
		input.close();

		BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		while ((line = errReader.readLine()) != null) {
			System.out.println(line);
		}

		System.out.flush();
		int retCode = process.waitFor();
		System.out.println("Return code: " + retCode);
	}

	
	public void doRep()
	{
	      System.out.println("File system roots returned by   FileSystemView.getFileSystemView():");
	      FileSystemView fsv = FileSystemView.getFileSystemView();
	      File[] roots = fsv.getRoots();
	      for (int i = 0; i < roots.length; i++)
	      {
	        System.out.println("Root: " + roots[i]);
	      }

	      System.out.println("Home directory: " + fsv.getHomeDirectory());

	      System.out.println("File system roots returned by File.listRoots():");

	      File[] f = File.listRoots();
	      for (int i = 0; i < f.length; i++)
	      {
	        System.out.println("Drive: " + f[i]);
	        System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
	        System.out.println("Is drive: " + fsv.isDrive(f[i]));
	        System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
	        System.out.println("Readable: " + f[i].canRead());
	        System.out.println("Writable: " + f[i].canWrite());
	      }
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.elisaxui.core.xui.XUIFactory#doSubFile(com.elisaxui.core.xui.XUIFactory.
	 * RequestConfig, com.elisaxui.core.xui.xhtml.XHTMLFile)
	 */
	@Override
	protected void doSubFile(RequestConfig requestConfig, XHTMLFile fileXML) {
		// TODO Auto-generated method stub

	}

}
