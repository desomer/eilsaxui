/**
 * 
 */
package com.elisaxui.core.xui.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.Atomic.Var;

import com.elisaxui.core.helper.FileComparator;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUIFactory;

/**
 * @author gauth
 *
 */
public class CacheManager {
	private static boolean enableCache = true;
	
	private static Map<String, ArrayList<String>> listeVersionId = null; 
	private static HashMap<String, String> resourceCacheMem = new HashMap<>() ;

	/*************************************************************************/
	private static DB db = null;
	public static HTreeMap<String, String> resourceDB = null;
	private static  Var<Object> listeDico= null;
	private static  Var<Object> lastDateFile= null;
	/**************************************************************************/
	private FileComparator fileComparator = new FileComparator();
	
	/*************************************************************************/
	public static final long getLastDate() {
		Long r = (Long) lastDateFile.get();
		if (r==null)
		{
			lastDateFile.set(System.currentTimeMillis());
			r = (Long) lastDateFile.get();
		}
		return r;
	}
	
	/**
	 * @param lastDate the lastDate to set
	 */
	public static final void setLastDate(long lastDate) {
		lastDateFile.set(lastDate);
		db.commit();
	}
	
	static {	
		initDB();
	}

	@SuppressWarnings("unchecked")
	private static void initDB() {
		String home = System.getProperty("user.home");
		String pathdb = home+ File.separator+"JTS"+ File.separator+"fileMapdb.db";
		
		CoreLogger.getLogger(1).info(()->"start db at "+pathdb);
		
		db = DBMaker.fileDB(pathdb).checksumHeaderBypass().closeOnJvmShutdown().make();	
		resourceDB = db.hashMap("mapFileHtml",Serializer.STRING,Serializer.STRING).createOrOpen();
		listeDico = db.atomicVar("pageDico").createOrOpen();
		lastDateFile = db.atomicVar("lastDate").createOrOpen();
		
		listeVersionId =  (Map<String, ArrayList<String>>) listeDico.get();
		if (listeVersionId==null)
			listeVersionId = new HashMap<>();
	}
	/************************************************/
	String id = null;
	String cachetype = null;   // es5
	String extension = null;
	boolean store=true;
	
	
	/**
	 * @return the store
	 */
	public final boolean isStore() {
		return store;
	}

	/**
	 * @param id
	 * @param cachetype
	 */
	public CacheManager(String id, String cachetype, String extension, boolean store) {
		super();
		this.id = id;
		this.cachetype = cachetype;
		this.extension = extension;
		this.store=store;
	}

	/**********************************************/
	private String idCache = null;   // id + date fichier+ type
	private String idCacheDicoVersionning = null;  // id + type
	String idCacheDB; 
	private String result = null;
	/************************************************/
	/**
	 * @return the idCache
	 */
	public final String getIdCacheDB() {
		return idCacheDB;
	}
	
	
	public void initVersion(boolean noCache, int version)
	{

		if (enableCache)
		{
			idCacheDicoVersionning = id+cachetype;
			idCache = id+XUIFactory.changeMgr.lastOlderFile+cachetype;
				
			if (!noCache)
			{
				setResult(getVersionDB(version));
			}	
		}
	}
	
	/**
	 *   si version = 0   => comparaison par rapport a la precedente
	 *   si version < 0   => comparaison par rapport a la courant
	 */
	public String getVersionDB(int version)
	{
		if (idCache==null)
			return null;
		
		int verDB = version;
		String htmlInCacheMoreRecent = null;
		if (version==0)
		{
			if (getResult()==null)
				// recherche en base
				setResult(resourceCacheMem.computeIfAbsent(idCache, k-> resourceDB.get(k)));
			
			htmlInCacheMoreRecent = getResult();
			verDB = -1;  // compare par rapport au dernier
		}
		else
			htmlInCacheMoreRecent= resourceDB.get(idCache);
		
		idCacheDB = idCache;
		/**************************************************************/
		/** recupere la version de comparaison */
		ArrayList<String> version1 = listeVersionId.get(idCacheDicoVersionning);
		if (version1!=null &&  -verDB<version1.size())
			idCacheDB = version1.get(version1.size()+verDB-1);
		String htmlInCacheDB = resourceDB.get(idCacheDB);
		/**************************************************************/
		
		// initialize 
		if (htmlInCacheDB!=null && htmlInCacheMoreRecent!=null) {
			getFileComparator().initLineDiff(htmlInCacheDB ,  htmlInCacheMoreRecent, extension.equals("html"));
		}
		
		if (version==0)
		{
			idCacheDB = idCache;    // affecte la version a tracer dans le log
			return getResult();
		}
		else
		{
			return htmlInCacheDB; 	
		}
	}
	
	public void commit()
	{
		
		Thread r = new Thread(new Runnable() {

			@Override
			public void run() {
				db.commit();
			}});
				
		r.start();
	}
	
	public void storeResultInDb(boolean commit) {
		if (idCache==null)
			return;

		CoreLogger.getLogger(2).info(()->"****** add cache "+idCache);
		
		ArrayList<String> version1 = listeVersionId.get(idCacheDicoVersionning);
		if (version1==null)
		{
			version1=new ArrayList<>();
			listeVersionId.put(idCacheDicoVersionning, version1);
		}
		version1.add(idCache);
		
		resourceDB.put(idCache, getResult());
		listeDico.set(listeVersionId);
		if (commit)
			db.commit();
		
		resourceCacheMem.put(idCache, getResult());
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the fileComparator
	 */
	public FileComparator getFileComparator() {
		return fileComparator;
	}

	/**
	 * @param fileComparator the fileComparator to set
	 */
	public void setFileComparator(FileComparator fileComparator) {
		this.fileComparator = fileComparator;
	}
}
