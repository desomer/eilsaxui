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
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.app.CacheManager;
import com.elisaxui.core.xui.app.ConfigFormat;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;

/**
 * @author gauth
 *
 */
public class XUIFactoryCordova extends XUIFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		CoreLogger.getLogger(1).info("lancer MTPDrive (icone appareil photo) et connecter en USB (avec un bon cable !)");
		
		XUIFactoryCordova builder = new XUIFactoryCordova();

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
		if (err!=null)
		{
			CoreLogger.getLogger(1).severe(() -> "ERR createVersion " + err);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    File apkFile = new File("C:\\Users\\gauth\\cordova\\test\\test\\platforms\\android\\app\\build\\outputs\\apk\\debug\\app-debug.apk");

	    long dateApk = apkFile.lastModified();
	    Stream<Path> listPath = null;
		try {
			Path path = Paths.get("C:\\Users\\gauth\\cordova\\test\\test\\www\\index.html");
			byte[] strToBytes = cache.getResult().getBytes();
			
			Files.write(path, strToBytes);

			// String read = Files.readAllLines(path).get(0);

			CoreLogger.getLogger(1).info(() -> "OK HTML " + path);
			
			createApk();
			
			CoreLogger.getLogger(1).info(() -> "OK APk " + apkFile);
			
			Path pathHuawei = Paths.get("Z:\\elysys");
			
			if (dateApk != apkFile.lastModified())
			{
				CoreLogger.getLogger(1).info(() -> "OK APK " + apkFile + " " +sdf.format(apkFile.lastModified()));
				Path pathApk = Paths.get("Z:\\elysys\\elisys.apk");
				Files.copy(apkFile.toPath(), pathApk, StandardCopyOption.REPLACE_EXISTING);
				CoreLogger.getLogger(1).info(() -> "OK COPY " + pathApk);
			}
						
			listPath = Files.walk(pathHuawei);
		
			listPath.filter(Files::isRegularFile)
		        .forEach((p)->{
		        	CoreLogger.getLogger(1).info(() -> "->"+p.toFile()+ " " +sdf.format(p.toFile().lastModified()));
		        });
						
			
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			listPath.close();
		}

		CoreLogger.getLogger(1).info("Terminé");
		
	}

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void createApk() throws IOException, InterruptedException {
		CoreLogger.getLogger(1).info(() -> "Tapez EXIT à la fin du script cordova");
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
